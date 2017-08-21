package pocket.pvp.child_effect;

/**
 * 受到特殊伤害%+-
 * @author 陈鹏
 */
public class Child_Effect_119 extends Child_Effect{
	
	public Child_Effect_119(int _target, String _targetValue, int _id) {
		super(_target, _targetValue, _id);
	}

	@Override
	public double TeDamagePer() {
		return GetPer();
	}
	
	
}
