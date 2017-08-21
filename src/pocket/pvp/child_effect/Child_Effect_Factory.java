package pocket.pvp.child_effect;

public class Child_Effect_Factory {

	private static Child_Effect_Factory instance;
	public static Child_Effect_Factory getInstance(){
		if(instance == null)
			instance = new Child_Effect_Factory();
		return instance;
	}
	
	public Child_Effect_Factory() {

	}
	
	// 创建异常状态效果
	public Child_Effect create(String effectId, int target, String targetValue){
		switch (effectId) {
		case "101":
			return new Child_Effect_101(target, targetValue, 101);
		case "102":
			return new Child_Effect_102(target, targetValue, 102);
		case "103":
			return new Child_Effect_103(target, targetValue, 103);
		case "104":
			return new Child_Effect_104(target, targetValue, 104);
		case "105":
			return new Child_Effect_105(target, targetValue, 105);
		case "106":
			return new Child_Effect_106(target, targetValue, 106);
		case "107":
			return new Child_Effect_107(target, targetValue, 107);
		case "108":
			return new Child_Effect_108(target, targetValue, 108);
		case "109":
			return new Child_Effect_109(target, targetValue, 109);
		case "110":
			return new Child_Effect_110(target, targetValue, 110);
		case "111":
			return new Child_Effect_111(target, targetValue, 111);
		case "112":
			return new Child_Effect_112(target, targetValue, 112);
		case "113":
			return new Child_Effect_113(target, targetValue, 113);
		case "114":
			return new Child_Effect_114(target, targetValue, 114);
		case "115":
			return new Child_Effect_115(target, targetValue, 115);
		case "116":
			return new Child_Effect_116(target, targetValue, 116);
		case "117":
			return new Child_Effect_117(target, targetValue, 117);
		case "118":
			return new Child_Effect_118(target, targetValue, 118);
		case "119":
			return new Child_Effect_119(target, targetValue, 119);
		case "120":
			return new Child_Effect_120(target, targetValue, 120);
		default:
			System.out.println("子效果effectId有误！");
			return null;
		}
	}
}
