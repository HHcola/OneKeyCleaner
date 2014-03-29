package my.example.onekeycleaner.db.table;

public class AppCategoryTable extends BaseTable {

	private final static String TABLE_NAME = "category";

	private final String num = "num";
	private final String cateName = "cateName";
	private final String cateId = "cateId";
	private final String cateIcon = "cateIcon";
	private final String cateType = "cateType";// 1 - soft， 2 - game

	public AppCategoryTable() {
		super(TABLE_NAME);
	}

	@Override
	public String getCreateSQL() {
		StringBuffer sql = new StringBuffer();
		
		sql.append("CREATE TABLE IF NOT EXISTS ");
		sql.append(TABLE_NAME);
		sql.append('(');
		sql.append(ID).append(" INTEGER PRIMARY KEY NOT NULL,");
		sql.append(num).append(" INTEGER NOT NULL,");
		sql.append(cateId).append(" INTEGER NOT NULL,");
		sql.append(cateName).append(" TEXT NOT NULL,");
		sql.append(cateIcon).append(" TEXT,");
		sql.append(cateType).append(" INTEGER,");
		sql.append(TIME).append(" INTEGER ");
		sql.append(");");
		
		return sql.toString();
	}

	@Override
	public String getDropSQL() {
		return "DROP TABLE IF EXISTS " + TABLE_NAME;
	}

	@Override
	public String[] getIndexColumns() {
		return new String[] { num, cateName, cateId, cateIcon, cateType};
	}

	public String getNum() {
		return num;
	}

	public String getCateName() {
		return cateName;
	}

	public String getCateId() {
		return cateId;
	}

	public String getCateIcon() {
		return cateIcon;
	}
	
	public String getCateType() {
		return cateType;
	}

}
