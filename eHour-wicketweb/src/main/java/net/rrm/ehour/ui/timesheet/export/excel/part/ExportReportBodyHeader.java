/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.timesheet.export.excel.part;

import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.report.excel.CellFactory;
import net.rrm.ehour.ui.common.report.excel.CellStyle;
import net.rrm.ehour.ui.common.report.excel.ExcelWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.wicket.model.ResourceModel;

/**
 * Created on Mar 25, 2009, 7:16:55 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportReportBodyHeader extends AbstractExportReportPart
{
	public ExportReportBodyHeader(int cellMargin, HSSFSheet sheet, Report report, ExcelWorkbook workbook)
	{
		super(cellMargin, sheet, report, workbook);
	}

	
	public int createPart(int rowNumber)
	{
		HSSFSheet sheet = getSheet();
        ExcelWorkbook workbook = getWorkbook();
		int cellMargin = getCellMargin();
		
		HSSFRow row = sheet.createRow(rowNumber);
		
        CellFactory.createCell(row, cellMargin + ExportReportColumn.DATE.getColumn(), new ResourceModel("excelMonth.body.date"), workbook, CellStyle.BOLD_BORDER_SOUTH);
        CellFactory.createCell(row, cellMargin + ExportReportColumn.CUSTOMER_CODE.getColumn(), new ResourceModel("excelMonth.body.customerCode"), workbook, CellStyle.BOLD_BORDER_SOUTH);
        CellFactory.createCell(row, cellMargin + ExportReportColumn.PROJECT.getColumn(), new ResourceModel("excelMonth.body.project"), workbook, CellStyle.BOLD_BORDER_SOUTH);
        CellFactory.createCell(row, cellMargin + ExportReportColumn.PROJECT_CODE.getColumn(), new ResourceModel("excelMonth.body.projectCode"), workbook, CellStyle.BOLD_BORDER_SOUTH);

		createEmptyCells(row, CellStyle.BORDER_SOUTH);

		CellFactory.createCell(row, cellMargin + ExportReportColumn.HOURS.getColumn(), new ResourceModel("excelMonth.body.hours"), workbook, CellStyle.BOLD_BORDER_SOUTH);
		
		rowNumber++;
		
		return rowNumber;
	}
}
