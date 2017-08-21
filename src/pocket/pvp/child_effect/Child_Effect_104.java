package pocket.pvp.child_effect;

/**
 * 物理防御%+-
 * @author 陈鹏
 */
public class Child_Effect_104 extends Child_Effect{

	public Child_Effect_104(int _target, String _targetValue, int _id) {
		super(_target, _targetValue, _id);
	}
	
	@Override
	public double GetWuDefensePer() {
		return super.GetPer();
	}
	
}
