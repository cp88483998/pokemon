package pocket.pvp.child_effect;

/**
 * 特殊攻击%+-
 * @author 陈鹏
 */
public class Child_Effect_105 extends Child_Effect{
	
	public Child_Effect_105(int _target, String _targetValue, int _id) {
		super(_target, _targetValue, _id);
	}

	@Override
	public double GetTeAttackPer() {
		return super.GetPer();
	}

	
}
