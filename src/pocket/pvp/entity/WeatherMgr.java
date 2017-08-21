package pocket.pvp.entity;

public class WeatherMgr {
	private static WeatherMgr instance;
	public static WeatherMgr getInstance(){
		if(instance == null)
			instance = new WeatherMgr();
		return instance;
	}
	private WeatherInfor weatherInfor;
	//上一次天气
	private WeatherType curWeatherType;
	
	public WeatherType getCurWeatherType() {
		return curWeatherType;
	}
	
	public void setCurWeatherType(WeatherType curWeatherType) {
		this.curWeatherType = curWeatherType;
	}

	//是否存在该天气
	public boolean IsHaveWeather(WeatherType weatherType){
		return curWeatherType==weatherType;
	}
	
	//是否存在该天气
	public boolean IsHaveWeather(int weatherType){
	    return curWeatherType == getCurWeatherType();
	}
	
	
}

//天气信息,天气的持续时间有两种可能（持续回合，或者精灵下场时）
class WeatherInfor
{

    /// <summary>
    /// 天气优先级
    /// </summary>
    public int  priority;

    /// <summary>
    /// 持续回合
    /// </summary>
    public int round;


    public int index;

    /// <summary>
    /// 目标对象
    /// </summary>
  //  public RoleElves targetRoleElves;

    /// <summary>
    /// 
    /// </summary>
    /// <param name="priority"></param>
    /// <param name="round"></param>  -1表示精灵下场移除
    public WeatherInfor(int index,int priority, int round)//, RoleElves targetRoleElves)
    {
        this.index = index;
        this.priority = priority;
        this.round = round;

      //  this.targetRoleElves = targetRoleElves;
    }

    public WeatherInfor Clone()
    {
        return new WeatherInfor(index,priority, round);
    }
    

    
    private WeatherType getWeatherType(int i){
    	switch (i) {
    	case 1:
    		return WeatherType.XiaYu;
    	case 2:
    		return WeatherType.YangGuang;
    	case 3:
    		return WeatherType.BingBao;
    	case 4:
    		return WeatherType.ShaBao;
    	case 5:
    		return WeatherType.DaWu;
    	case 6:
    		return WeatherType.XiaDaYu;
    	case 7:
    		return WeatherType.RiGuangMengLie;
    	case 8:
    		return WeatherType.KongQiDuanLiu;
    	default:
    		break;
    	}
    	return WeatherType.None;
    }
}

//天气类型(8个)
enum WeatherType{
    XiaYu,
    YangGuang,

    BingBao,
    ShaBao,

    DaWu,
    XiaDaYu,
    RiGuangMengLie,

    KongQiDuanLiu,

    None
}

