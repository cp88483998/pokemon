package pocket.pvp.child_effect;

/**
 * 特防等级+-
 * @author 陈鹏
 */
public class Child_Effect_115 extends Child_Effect{
	
	public Child_Effect_115(int _target, String _targetValue, int _id) {
		super(_target, _targetValue, _id);
	}

	@Override
	public int GetTeDefenseLevel() {
		return targetValueInt();
	}
	
	
}
