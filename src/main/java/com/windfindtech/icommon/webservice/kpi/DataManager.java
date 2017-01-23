package com.windfindtech.icommon.webservice.kpi;

import com.google.gson.Gson;
import com.windfindtech.icommon.gson.GsonUtil;
import com.windfindtech.icommon.jsondata.shanghai.CarPlateData;
import com.windfindtech.icommon.jsondata.shanghai.FuelData;
import com.windfindtech.icommon.jsondata.shanghai.FuelTrendData;
import com.windfindtech.icommon.jsondata.shanghai.ISHBaseData;
import com.windfindtech.icommon.jsondata.shanghai.LotteryData;
import com.windfindtech.icommon.jsondata.shanghai.MovieData;
import com.windfindtech.icommon.jsondata.shanghai.NecessityData;
import com.windfindtech.icommon.jsondata.shanghai.RoadClosureData;
import com.windfindtech.icommon.jsondata.shanghai.SportsData;
import com.windfindtech.icommon.jsondata.shanghai.StockIndexData;
import com.windfindtech.icommon.jsondata.shanghai.SurfaceWaterData;
import com.windfindtech.icommon.jsondata.shanghai.TrafficData;
import com.windfindtech.icommon.jsondata.shanghai.WaterQualityData;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.retrofit.StreamParser;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

import org.pmw.tinylog.Logger;

import java.io.InputStreamReader;

/**
 * Created by cplu on 2015/3/11.
 * 管理获取所有KPI信息的数据
 */
public class DataManager {
	//	private static final Logger logger = Logger.getLogger(DataManager.class);
	public static final int CAR_PLATE_IDX = 0;
	public static final int STOCK_IDX = 1;
	public static final int FUEL_IDX = 2;
	public static final int TRAFFIC_IDX = 3;
	public static final int SURFACE_WATER_IDX = 4;
	public static final int NECESSITY_IDX = 5;
	public static final int LOTTERY_IDX = 6;
	public static final int SPORTS_IDX = 7;
	public static final int ROAD_CLOSURE_IDX = 8;
	public static final int SH_BLOCK_NUMBER = 9;
	/// other data in addition to block data
	public static final int MOVIE_INFO_IDX = 9; // movie infos shown at bottom of ShanghaiFragment
	public static final int FUEL_TREND_IDX = 10;    // fuel trend data
	public static final int CAR_PLATE_TREND_IDX = 11;   // car plate trend data
	public static final int WATER_QUALITY_LEGACY_IDX = 12;  /// water quality data for legacy compatibility
	public static final int ALL_DATA_NUMBER = 13;

	private static DataManager m_inst;
//    private ResponseTimeoutChecker[] m_timeoutCheckers = new ResponseTimeoutChecker[ALL_DATA_NUMBER];
//    private Handler m_handler;

	//    private long[] m_last_fetching_times_in_ms = new long[ALL_DATA_NUMBER];
	private ISHBaseData[] m_data = new ISHBaseData[ALL_DATA_NUMBER];
	private static final int[] DATA_TIMEOUTS = new int[]{
			12 * 60 * 60 * 1000, /// car plate, 12 hour
			1 * 60 * 1000,  /// stock data, 1 minute
			12 * 60 * 60 * 1000, /// fuel data, 12 hour
			5 * 60 * 1000,  /// traffic data, 5 minute
			6 * 60 * 60 * 1000, /// water quality, 6 hour
			60 * 60 * 1000, /// necessity data, 1 hour
			60 * 60 * 1000, /// lottery data, 1 hour
			2 * 60 * 60 * 1000, /// sports data, 2 hour
			60 * 60 * 1000, /// road closure, 1 hour
			30 * 60 * 1000, /// movie data, 30 minute
			12 * 60 * 60 * 1000, /// fuel trend, 12 hour
			12 * 60 * 60 * 1000, /// car plate trend, 12 hour
			12 * 60 * 60 * 1000, /// water quality (legacy), 12 hour
	};
	private static final String[] DATA_URLS = new String[]{
			"api/info/license_auction",
			"api/info/finance/indexes/ssec",
			"api/info/gasoline",
			"api/info/traffic",
			"api/info/surface_water_quality",
			"api/info/food_price",
			"api/info/lottery",
			"api/info/csl",
			"api/info/closure_plan",
			"api/info/movies",
			"api/info/gasoline/recent",
			"api/info/license_auction/recent",
			"api/info/water_quality",
	};
	private static final Class[] DATA_CLAZZES = new Class[]{
			CarPlateData.class,
			StockIndexData.class,
			FuelData.class,
			TrafficData.class,
			SurfaceWaterData.class,
			NecessityData.class,
			LotteryData.class,
			SportsData.class,
			RoadClosureData.class,
			MovieData.class,
			FuelTrendData.class,
			CarPlateData.class,
			WaterQualityData.class
	};

	/**
	 * parse an array of data to a json object, with "data" as key
	 * @param <T>       the returned type of data
	 */
	private static class ArrayParser<T> implements StreamParser<T> {
		private final Class<T> m_clazz;

		public ArrayParser(Class<T> clazz) {
			m_clazz = clazz;
		}

		@Override
		public T parse(InputStreamReader reader) {
			/// parse array to an object, by prefixing <{"data":> and suffixing <}>
			final int bufferSize = 1024;
			final char[] buffer = new char[bufferSize];
			try {
				final StringBuilder out = new StringBuilder("{\"data\":");
				for (; ; ) {
					int rsz = reader.read(buffer, 0, buffer.length);
					if (rsz < 0) {
						break;
					}
					out.append(buffer, 0, rsz);
				}
				out.append("}");
				Gson gson = GsonUtil.getGson();
				return gson.fromJson(out.toString(), m_clazz);
			} catch (Exception e) {
				Logger.error(e);
				return null;
			}
		}
	}

	private static class RoadClosureDataParser implements StreamParser<RoadClosureData> {
		@Override
		public RoadClosureData parse(InputStreamReader reader) {
			Gson gson = GsonUtil.getGson();
			RoadClosureData data = gson.fromJson(reader, RoadClosureData.class);
			data.aggregateData();
			return data;
		}
	}

	private static final StreamParser[] DATA_PARSER = new StreamParser[]{
			new ArrayParser(DATA_CLAZZES[CAR_PLATE_IDX]),           /// car plate data is in array structure
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			new RoadClosureDataParser(),
			null,
			new ArrayParser(DATA_CLAZZES[FUEL_TREND_IDX]),
			new ArrayParser(DATA_CLAZZES[CAR_PLATE_TREND_IDX]),
			null
	};
//    private final boolean[] m_is_fetching = new boolean[]{
//            false,
//            false,
//            false,
//            false,
//            false,
//            false,
//            false,
//            false,
//            false,
//            false
//    };

	private DataManager() {
//        m_handler = new Handler();  /// should be in ui thread
//	    for(int i = 0; i < m_timeoutCheckers.length; i++) {
//		    m_timeoutCheckers[i] = new ResponseTimeoutChecker(DATA_TIMEOUTS[i], DATA_CLAZZES[i]);
//	    }
	}

	public static DataManager instance() {
		if (m_inst == null) {
			m_inst = new DataManager();
		}
		return m_inst;
	}

	/**
	 * retrieve kpi data from web service
	 *
	 * @param index        which data is to be retrieved, the index is listed at the beginning of class DataManager
	 * @param useCache     whether we could use cached data
	 * @param callback     the callback of data
	 */
	public void retrieveShData(final int index, boolean useCache, final WSCallback<ISHBaseData> callback) {
		WSCallback<ISHBaseData> requestCallback = new WSCallback<ISHBaseData>() {
			@Override
			public void onSuccess(ISHBaseData ret) {
				m_data[index] = ret;
				callback.onSuccess(ret);
			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				callback.onFailed(reason);
			}
		};
		WSManager.instance().doHttpGet(DATA_URLS[index], DATA_CLAZZES[index], DATA_PARSER[index], DATA_TIMEOUTS[index], useCache,
				requestCallback);
	}

//    public void clearData() {
//        for(int i = 0; i < ALL_DATA_NUMBER; i++){
//            m_datas[i] = null;
//        }
//    }

	public ISHBaseData getData(int index) {
		return m_data[index];
	}
}
