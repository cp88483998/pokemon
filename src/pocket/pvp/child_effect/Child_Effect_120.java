package pocket.pvp.child_effect;

/**
 * 无法行动			//精灵跳过行动
 * @author 陈鹏
 */
public class Child_Effect_120 extends Child_Effect{
	
	public Child_Effect_120(int _target, String _targetValue, int _id) {
		super(_target, _targetValue, _id);
	}

	@Override
	public void init(Object arges) {
		super.init(arges);
		isCanntAction = true;
	}
	
}
