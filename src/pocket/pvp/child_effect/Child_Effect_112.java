package pocket.pvp.child_effect;

/**
 * 物攻等级+-
 * @author 陈鹏
 */
public class Child_Effect_112 extends Child_Effect{
	
	public Child_Effect_112(int _target, String _targetValue, int _id) {
		super(_target, _targetValue, _id);
	}

	@Override
	public int GetWuAttackLevel() {
		return targetValueInt();
	}
	
	
}
