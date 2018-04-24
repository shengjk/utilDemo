package util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.HashSet;
import java.util.Iterator;

/*
先遍历每一个sheet，然后遍历每一个sheet的每一行，然后遍历每一行的每一列
 */
public class ReadExcel {
	
	public static void read(InputStream inputStream) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		/*
          获取单个sheet 页，单个单元格的数据。
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row0 = sheet.getRow(0);
        XSSFCell cell = row0.getCell(0);
        System.out.println(cell.getRichStringCellValue().getString());*/
		//获取excel表格中的所有数据
		//workbook.getNumberOfSheets(); 获取sheet 页个数。
		int sheetNum = workbook.getNumberOfSheets();
		//System.out.println(sheetNum);
		//for循环遍历单元格内容   遍历sheet
		for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {
			//根据下标获取sheet
			XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
			//workbook.getSheetName(sheetIndex) 根据下标获取sheet 名称
			System.out.println("# sheet序号：" + sheetIndex + "，sheet名称：" + workbook.getSheetName(sheetIndex));
			//循环该sheet页中的有数据的每一行
			//打印行号，某人起始是0  System.out.println(sheet.getLastRowNum());
			//打印行数
//			System.out.println(sheet.getPhysicalNumberOfRows());
		
			//insert into table userinfos2 select id,age,name from userinfos;
			
			
			String createSql="create EXTERNAL table "+workbook.getSheetName(sheetIndex)+" ( " ;
			String insertSql="insert overwrite table "+workbook.getSheetName(sheetIndex)+" select " ;
			HashSet<String> insertTableName =new HashSet<String>();
			String insertWHere="";
			
			//遍历每行内容从行号为0开始  遍历每一行
			for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
				//System.out.println(rowIndex);打印遍历行号
				//根据行号，遍历该行
				XSSFRow row = sheet.getRow(rowIndex);
				//如果该行为空，则结束本次循环
				if (row == null) {
					continue;
				}
				//num 为该行 有效单元格个数，取 num的话，取值会不全。   lastnum为 有效单元格最后各个单元格的列号，这样可以遍历取到该行所有的单元格
//				System.out.println("num  " + row.getPhysicalNumberOfCells());
//				System.out.println("lastnum " + row.getLastCellNum());
				
				
//				遍历每一列
				for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
					XSSFCell cell = row.getCell(cellnum);
					if (cell != null) {
						
						cell.setCellType(Cell.CELL_TYPE_STRING);
						//cell.setCellType(Cell.CELL_TYPE_STRING); 是为了修改数据类型，因为我的单元格中有数字类型。如果不这样写会出现下面的错误。
                /*        Exception in thread "main" java.lang.IllegalStateException:
                         Cannot get a text value from a numeric cell
                        at org.apache.poi.xssf.usermodel.XSSFCell.typeMismatch(XSSFCell.java:991)
                        at org.apache.poi.xssf.usermodel.XSSFCell.getRichStringCellValue(XSSFCell.java:399)
                        at net.oschina.excel.ReadExcel.read(ReadExcel.java:55)
                        at net.oschina.excel.ReadExcel.main(ReadExcel.java:68)
                        
                        POI操作Excel时数据Cell有不同的类型，当我们试图从一个数字类型的Cell读取出一个字符串并写入数据库时，
                        就会出现Cannot get a text value from a numeric cell的异常错误，解决办法就是先设置Cell的类型，
                        然后就可以把纯数字作为String类型读进来了：
                        
                        
                        alter table aaaa SET TBLPROPERTIES('EXTERNAL'='TRUE');
 						create  table aaaa STORED AS parquet  as select * from test;
 						create EXTERNAL table tableName(,) STORED AS parquet
                        */
						//打印出读出的数据。
						
						if(cellnum==2 && rowIndex>0){
							String cellStr=cell.getRichStringCellValue().getString();
							if(cellStr==null|| cellStr.trim().length()==0){
								System.out.println("sheet名称：" + workbook.getSheetName(sheetIndex)+" 第" + rowIndex + "行      第" + cellnum + "列    内容为：空 " + cell.getRichStringCellValue().getString());
							}
							createSql=createSql+cellStr+" ";
						}
						
						if(cellnum==5 && rowIndex>0){
							String cellStr=cell.getRichStringCellValue().getString();
							if(workbook.getSheetName(sheetIndex).contains("tbl.ods_evt_colrec")  && rowIndex==10){
								System.out.println(cellStr);
							}
							if(cellStr==null|| cellStr.trim().length()==0){
								System.out.println("sheet名称：" + workbook.getSheetName(sheetIndex)+" 第" + rowIndex + "行      第" + cellnum + "列    内容为：空 " + cell.getRichStringCellValue().getString());
							}
							if(cellStr.contains("char") || cellStr.contains("money")){
								cellStr="string";
							}else if(cellStr.contains("int")){
								cellStr="bigint";
							}
							if (rowIndex == sheet.getPhysicalNumberOfRows()-1){
								
								createSql=createSql+cellStr;
							}else {
								createSql=createSql+cellStr+", ";
							}
						}
						
						if(cellnum==7 && rowIndex>0){
							//billomm.
							//source_table
							String cellStr=cell.getRichStringCellValue().getString();
							insertTableName.add(cellStr);
							//source_field
							String[] tableName=cellStr.split("_",-1);
							String tableOtherName=tableName[tableName.length-1];
							
							XSSFCell cellTmp = row.getCell(cellnum+2);
							
							if (cellTmp != null) {
								String sourceFieldStr=cellTmp.getRichStringCellValue().getString();
								String othName=row.getCell(2).getRichStringCellValue().getString();
								if(rowIndex ==sheet.getPhysicalNumberOfRows()-1){
									
									insertSql=insertSql+tableOtherName+"."+sourceFieldStr+" as "+othName;
								}else {
									insertSql=insertSql+tableOtherName+"."+sourceFieldStr+" as "+othName+", ";
									
								}
								
								XSSFCell cellTmpQuery = row.getCell(cellnum+6);
								
								if (cellTmpQuery != null) {
									String sourceFieldQueryStr=cellTmpQuery.getRichStringCellValue().getString();
									insertWHere=insertWHere+"  "+sourceFieldQueryStr+" ";
									
									
								}else {
//									if ((cellnum == 7 || cellnum == 9 || cellnum == 13) && rowIndex > 0) {
//										System.out.println("sheet名称：" + workbook.getSheetName(sheetIndex) + " 第" + rowIndex + "行      第" + cellnum + "列    内容为：空 ");
//
//									}
								}
							
							}else {
								if((cellnum==7 ||cellnum==9) && rowIndex>0){
									System.out.println("sheet名称：" + workbook.getSheetName(sheetIndex)+" 第" + rowIndex + "行      第" + cellnum + "列    内容为：空 ");
									
								}

							}
						}
						
						
//						System.out.println("第" + rowIndex + "行      第" + cellnum + "列    内容为：" + cell.getRichStringCellValue().getString());
					}else{
						if((cellnum==2 ||cellnum==5) && rowIndex>0){
							System.out.println("sheet名称：" + workbook.getSheetName(sheetIndex)+" 第" + rowIndex + "行      第" + cellnum + "列    内容为：空 ");
							
						}
					}
				}
			}
			createSql=createSql+") stored as parquet;";
			System.out.println(createSql);
			
			String insertTable="";
			int i=0;
			for(Iterator it = insertTableName.iterator(); it.hasNext();){
				
				if(i==insertTableName.size()-1){
					insertTable=insertTable+it.next();
				}else{
					insertTable=insertTable+it.next()+",";
				}
				i++;
			}
			
			String[] tableName=insertTable.split("_",-1);
			String tableOtherName=tableName[tableName.length-1];
			
			
			insertSql=insertSql+" from   "+insertTable+" as "+tableOtherName+" where "+insertWHere +" ;";
			System.out.println(insertSql);
			
			System.out.println("\n# ------------------+++++++++++++++++++--------------------");
		}
	}
	
	public static void main(String[] args) {
		InputStream inputStream = null;
		try {
			//获取文件标识符。
			inputStream = new FileInputStream(new File("E:\\工作\\一诺\\一诺数据平台资料（0518）\\一诺维度表(0608).xlsx"));
			//System.out.println(inputStream);
			read(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}