package pocket.pvp.child_effect;

/**
 * 特殊防御%+-
 * @author 陈鹏
 */
public class Child_Effect_106 extends Child_Effect{
	
	public Child_Effect_106(int _target, String _targetValue, int _id) {
		super(_target, _targetValue, _id);
	}

	@Override
	public double GetTeDefensePer() {
		return super.GetPer();
	}
	
}
