package pocket.total.util;


public class PGMultiple {
	/*
	 * 相克倍数计算
	 */
	public double mul_com(String str1, String str2){
		if(str1.equals("普通")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 1;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 1;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 0.5;
			}
			if(str2.equals("鬼")){
				return 0;
			}
			if(str2.equals("钢")){
				return 0.5;
			}
			if(str2.equals("火")){
				return 1;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 1;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 1;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}
		}
		if(str1.equals("格斗")){
			if(str2.equals("普通")){
				return 2;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 0.5;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 0.5;
			}
			if(str2.equals("虫")){
				return 0.5;
			}
			if(str2.equals("岩")){
				return 2;
			}
			if(str2.equals("鬼")){
				return 0;
			}
			if(str2.equals("钢")){
				return 2;
			}
			if(str2.equals("火")){
				return 1;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 1;
			}
			if(str2.equals("冰")){
				return 2;
			}
			if(str2.equals("超能")){
				return 0.5;
			}
			if(str2.equals("龙")){
				return 1;
			}
			if(str2.equals("恶")){
				return 2;
			}
			if(str2.equals("妖精(仙)")){
				return 0.5;
			}		
		}
		if(str1.equals("毒")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 0.5;
			}
			if(str2.equals("地")){
				return 0.5;
			}
			if(str2.equals("飞行")){
				return 1;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 0.5;
			}
			if(str2.equals("鬼")){
				return 0.5;
			}
			if(str2.equals("钢")){
				return 0;
			}
			if(str2.equals("火")){
				return 1;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 2;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 1;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 2;
			}
		}
		if(str1.equals("地")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 2;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 0;
			}
			if(str2.equals("虫")){
				return 0.5;
			}
			if(str2.equals("岩")){
				return 2;
			}
			if(str2.equals("鬼")){
				return 0;
			}
			if(str2.equals("钢")){
				return 2;
			}
			if(str2.equals("火")){
				return 2;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 2;	
			}
			if(str2.equals("草")){
				return 0.5;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 1;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}
		}
		if(str1.equals("飞行")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 2;	
			}
			if(str2.equals("毒")){
				return 1;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 1;
			}
			if(str2.equals("虫")){
				return 2;
			}
			if(str2.equals("岩")){
				return 0.5;
			}
			if(str2.equals("鬼")){
				return 1;
			}
			if(str2.equals("钢")){
				return 0.5;
			}
			if(str2.equals("火")){
				return 1;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 0.5;	
			}
			if(str2.equals("草")){
				return 2;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 1;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}
		}
		if(str1.equals("虫")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 0.5;	
			}
			if(str2.equals("毒")){
				return 0.5;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 0.5;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 1;
			}
			if(str2.equals("鬼")){
				return 0.5;
			}
			if(str2.equals("钢")){
				return 0.5;
			}
			if(str2.equals("火")){
				return 0.5;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 2;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 2;
			}
			if(str2.equals("龙")){
				return 1;
			}
			if(str2.equals("恶")){
				return 2;
			}
			if(str2.equals("妖精(仙)")){
				return 0.5;
			}
		}
		if(str1.equals("岩")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 0.5;	
			}
			if(str2.equals("毒")){
				return 1;
			}
			if(str2.equals("地")){
				return 0.5;
			}
			if(str2.equals("飞行")){
				return 2;
			}
			if(str2.equals("虫")){
				return 2;
			}
			if(str2.equals("岩")){
				return 1;
			}
			if(str2.equals("鬼")){
				return 1;
			}
			if(str2.equals("钢")){
				return 0.5;
			}
			if(str2.equals("火")){
				return 2;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 1;
			}
			if(str2.equals("冰")){
				return 2;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 1;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}
		}
		if(str1.equals("鬼")){
			if(str2.equals("普通")){
				return 0;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 1;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 1;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 1;
			}
			if(str2.equals("鬼")){
				return 2;
			}
			if(str2.equals("钢")){
				return 1;
			}
			if(str2.equals("火")){
				return 1;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 1;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 2;
			}
			if(str2.equals("龙")){
				return 1;
			}
			if(str2.equals("恶")){
				return 0.5;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}
		}
		if(str1.equals("钢")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 1;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 1;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 2;
			}
			if(str2.equals("鬼")){
				return 1;
			}
			if(str2.equals("钢")){
				return 0.5;
			}
			if(str2.equals("火")){
				return 0.5;
			}
			if(str2.equals("水")){
				return 0.5;
			}
			if(str2.equals("电")){
				return 0.5;	
			}
			if(str2.equals("草")){
				return 1;
			}
			if(str2.equals("冰")){
				return 2;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 1;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 2;
			}
		}
		if(str1.equals("火")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 1;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 1;
			}
			if(str2.equals("虫")){
				return 2;
			}
			if(str2.equals("岩")){
				return 0.5;
			}
			if(str2.equals("鬼")){
				return 1;
			}
			if(str2.equals("钢")){
				return 2;
			}
			if(str2.equals("火")){
				return 0.5;
			}
			if(str2.equals("水")){
				return 0.5;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 2;
			}
			if(str2.equals("冰")){
				return 2;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 0.5;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}
		}
		if(str1.equals("水")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 1;
			}
			if(str2.equals("地")){
				return 2;
			}
			if(str2.equals("飞行")){
				return 1;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 2;
			}
			if(str2.equals("鬼")){
				return 1;
			}
			if(str2.equals("钢")){
				return 1;
			}
			if(str2.equals("火")){
				return 2;
			}
			if(str2.equals("水")){
				return 0.5;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 0.5;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 0.5;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}
		}
		if(str1.equals("电")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 1;
			}
			if(str2.equals("地")){
				return 0;
			}
			if(str2.equals("飞行")){
				return 2;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 1;
			}
			if(str2.equals("鬼")){
				return 1;
			}
			if(str2.equals("钢")){
				return 1;
			}
			if(str2.equals("火")){
				return 1;
			}
			if(str2.equals("水")){
				return 2;
			}
			if(str2.equals("电")){
				return 0.5;	
			}
			if(str2.equals("草")){
				return 0.5;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 0.5;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}		
		}
		if(str1.equals("草")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 0.5;
			}
			if(str2.equals("地")){
				return 2;
			}
			if(str2.equals("飞行")){
				return 0.5;
			}
			if(str2.equals("虫")){
				return 0.5;
			}
			if(str2.equals("岩")){
				return 2;
			}
			if(str2.equals("鬼")){
				return 1;
			}
			if(str2.equals("钢")){
				return 0.5;
			}
			if(str2.equals("火")){
				return 0.5;
			}
			if(str2.equals("水")){
				return 2;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 0.5;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 0.5;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}
		}
		if(str1.equals("冰")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 1;
			}
			if(str2.equals("地")){
				return 2;
			}
			if(str2.equals("飞行")){
				return 2;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 1;
			}
			if(str2.equals("鬼")){
				return 1;
			}
			if(str2.equals("钢")){
				return 0.5;
			}
			if(str2.equals("火")){
				return 0.5;
			}
			if(str2.equals("水")){
				return 0.5;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 2;
			}
			if(str2.equals("冰")){
				return 0.5;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 2;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}
		}
		if(str1.equals("超能")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 2;	
			}
			if(str2.equals("毒")){
				return 2;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 1;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 1;
			}
			if(str2.equals("鬼")){
				return 1;
			}
			if(str2.equals("钢")){
				return 0.5;
			}
			if(str2.equals("火")){
				return 1;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 1;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 0.5;
			}
			if(str2.equals("龙")){
				return 1;
			}
			if(str2.equals("恶")){
				return 0;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}
		}
		if(str1.equals("龙")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 1;	
			}
			if(str2.equals("毒")){
				return 1;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 1;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 1;
			}
			if(str2.equals("鬼")){
				return 1;
			}
			if(str2.equals("钢")){
				return 0.5;
			}
			if(str2.equals("火")){
				return 1;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 1;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 2;
			}
			if(str2.equals("恶")){
				return 1;
			}
			if(str2.equals("妖精(仙)")){
				return 0;
			}
		}
		if(str1.equals("恶")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 0.5;	
			}
			if(str2.equals("毒")){
				return 1;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 1;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 1;
			}
			if(str2.equals("鬼")){
				return 2;
			}
			if(str2.equals("钢")){
				return 1;
			}
			if(str2.equals("火")){
				return 1;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 1;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 2;
			}
			if(str2.equals("龙")){
				return 1;
			}
			if(str2.equals("恶")){
				return 0.5;
			}
			if(str2.equals("妖精(仙)")){
				return 0.5;
			}
		}
		if(str1.equals("妖精(仙)")){
			if(str2.equals("普通")){
				return 1;
			}
			if(str2.equals("格斗")){
				return 2;	
			}
			if(str2.equals("毒")){
				return 0.5;
			}
			if(str2.equals("地")){
				return 1;
			}
			if(str2.equals("飞行")){
				return 1;
			}
			if(str2.equals("虫")){
				return 1;
			}
			if(str2.equals("岩")){
				return 1;
			}
			if(str2.equals("鬼")){
				return 1;
			}
			if(str2.equals("钢")){
				return 0.5;
			}
			if(str2.equals("火")){
				return 0.5;
			}
			if(str2.equals("水")){
				return 1;
			}
			if(str2.equals("电")){
				return 1;	
			}
			if(str2.equals("草")){
				return 1;
			}
			if(str2.equals("冰")){
				return 1;
			}
			if(str2.equals("超能")){
				return 1;
			}
			if(str2.equals("龙")){
				return 2;
			}
			if(str2.equals("恶")){
				return 2;
			}
			if(str2.equals("妖精(仙)")){
				return 1;
			}
		}
		return 0;
	}
}
