package pocket.pvp.entity;

public class BattleConst {
	public static int GetPowerIndexByHpPer(double per)
    {
        int range = (int)(64*per);

        if (IsInRange(0,1,range))
        {
            return 0;
        }
        else if (IsInRange(2, 5, range))
        {
            return 1;
        }
        else if (IsInRange(6, 12, range))
        {
            return 2;
        }
        else if (IsInRange(13, 21, range))
        {
            return 3;
        }
        else if (IsInRange(22, 42, range))
        {
            return 4;
        }
        else if (IsInRange(43, 64, range))
        {
            return 5;
        }

        return Integer.MAX_VALUE;

    }
	private static boolean IsInRange(int min, float max, int cur)
    {
        if (cur >= min && cur <= max)
        {
            return true;
        }

        return false;

    }
}
