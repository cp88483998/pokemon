package pocket.pvp.child_effect;

/**
 * 速度等级+-
 * @author 陈鹏
 */
public class Child_Effect_116 extends Child_Effect{
	
	public Child_Effect_116(int _target, String _targetValue, int _id) {
		super(_target, _targetValue, _id);
	}

	@Override
	public int GetSpeedLevel() {
		return targetValueInt();
	}
	
	
}
