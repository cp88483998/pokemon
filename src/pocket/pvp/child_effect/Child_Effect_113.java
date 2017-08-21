package pocket.pvp.child_effect;

/**
 * 特攻等级+-
 * @author 陈鹏
 */
public class Child_Effect_113 extends Child_Effect{
	
	public Child_Effect_113(int _target, String _targetValue, int _id) {
		super(_target, _targetValue, _id);
	}

	@Override
	public int GetTeAttackLevel() {
		return targetValueInt();
	}
	
	
}
