package pocket.total.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;
import pocket.pvp.entity.Elve;

public class MyUtil {
	//字符串类型的数字转换成int,如果value为null或者空，则返回int.MaxValue
	public static int IntParse(Object value)
    {

        if (value != null && value.toString() != "")
        {
            return Integer.parseInt(value.toString());
        }
        else
        {
            return Integer.MAX_VALUE;
        }
    }
	
	//监测是否为空
	public static boolean isNullOrEmpty(String str){
		if(str == null || str.isEmpty()){
			return true;
		}
		return false;
	}
	
	//String[] 变 int[]
	public static int[] ArrayToInt(String[] str){
		
		int[] arr = new int[str.length];
		for(int i=0;i<str.length;i++){
			arr[i] = Integer.parseInt(str[i]);
		}
		return arr;
	}
	//随机0~1
	public static float getRandomValue01(){
		Random ran = new Random();
		return ran.nextInt(100)/100f;
	}
	
	//安全转换 
	public static int[] SafeArrayToInt(String str, int num, String separator){
		if(isNullOrEmpty(str)){
			
		}else{
			int[] arr = ArrayToInt( str.split(separator));
			if (arr == null || arr.length != num){

            }else{
                return arr;
            }
		}
		int[] tmp = new int[num];
        for (int i = 0; i < num; i++){
            tmp[i] = Integer.MAX_VALUE;
        }
        return tmp;
	}
	public static String[] SafeArrayToString(String str, int num, String separator){
		if(isNullOrEmpty(str)){
			
		}else{
			String[] arr = str.split(separator);
			if (arr == null || arr.length != num){

            }else{
                return arr;
            }
		}
		String[] tmp = new String[num];
        for (int i = 0; i < num; i++){
            tmp[i] = "";
        }
        return tmp;
	}
	
	//转化成精灵需要的key
	public static String elvesKey(Elve elvesDataInforVo){
        if (elvesDataInforVo == null)
            return "";
        
        return elvesDataInforVo.geteLvesID() + "_" + elvesDataInforVo.getGender() + "_" + elvesDataInforVo.getCharacter() + "_" +
               elvesDataInforVo.getSameIndex();
    }
	
	public static int[] ArrayToIntSame(String str ,int num){
        int[] ints = new int[num];
        for (int i = 0; i < num; i++){
            ints[i] = Integer.parseInt(str);
        }
        return ints;
    }
	
	//set集合转数组
	public static float[] setToArray(HashSet<Float> set){
		Float[] temp = set.toArray(new Float[]{});
		float[] floatArray = new float[temp.length];
		for(int i=0;i<temp.length;i++){
			floatArray[i] = temp[i].floatValue();
		}
		return floatArray;
	}
	//随机n个不重复的float
	public static float[] ranFloatArr1(int n){
		HashSet<Float> set = new HashSet<Float>();
		Random ran = new Random();
		while(set.size()<n){
			float d = (ran.nextInt(100000001))/100000000f;
			set.add(d);
		}
		Float[] temp = set.toArray(new Float[]{});
		float[] floatArray = new float[temp.length];
		for(int i=0;i<temp.length;i++){
			floatArray[i] = temp[i].floatValue();
		}
//		System.out.println(floatArray.length);
		return floatArray;
	}
	
	public static String addElveId(Elve elve){
		String elvesId = elve.geteLvesID()+"_"+elve.getGender()+"_"+elve.getCharacter()+"_"+elve.getSameIndex();
		return elvesId;
	}
	
	public static int parseYesOrNo(boolean vital){
		if(vital){
			return 1;
		}else{
			return 0;//0：否 1：是
		}
	}
	
	public static List<Integer> jaToIntList(JSONArray ja){
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<ja.size();i++){
			list.add(ja.getInt(i));
		}
		return list;
	}

	public static List<String> jaToStrList(JSONArray ja){
		List<String> list = new ArrayList<String>();
		for(int i=0;i<ja.size();i++){
			list.add(ja.getString(i));
		}
		return list;
	}
	
	public static int random(int size){
		Random ran = new Random();
		int i = ran.nextInt(size);
		return i;
	}
	
	public static int random(int start, int end){
		Random ran = new Random();
		int i = ran.nextInt(end - start) + start;
		return i;
	}
	
	/*
	 * 随机四个0~end的随机数
	 */
	public static int[] get4Random(int end){
		int[] arr = new int[4];
		Random ran = new Random();
		int n;
		for(int i=0;i<4;i++){
			n = ran.nextInt(end);
			arr[i] = n;
			System.out.println(n);
		}
		return arr;
	}
}
