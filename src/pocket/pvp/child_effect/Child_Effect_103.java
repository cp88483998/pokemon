package pocket.pvp.child_effect;

/**
 * 物理攻击%+-
 * @author 陈鹏
 */
public class Child_Effect_103 extends Child_Effect{

	public Child_Effect_103(int _target, String _targetValue, int _id) {
		super(_target, _targetValue, _id);
	}
	
	@Override
	public double GetWuAttackPer() {
		return super.GetPer();
	}
	
}
