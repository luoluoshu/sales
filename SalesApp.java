package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

    private SalesDao salesDao = new SalesDao();
    SalesReportDao salesReportDao = new SalesReportDao();

    public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {

        if (salesId == null) {
            return;
        }

        Sales sales = getSalesBySaleId(salesId);

        if (isSalesEffective(sales)) return;

        List<SalesReportData> reportDataList = getSalesReportDataBySales(sales);

        List<SalesReportData> filteredReportDataList = new ArrayList<SalesReportData>();
        for (SalesReportData data : reportDataList) {
            if ("SalesActivity".equalsIgnoreCase(data.getType())) {
                if (data.isConfidential()) {
                    if (isSupervisor) {
                        filteredReportDataList.add(data);
                    }
                } else {
                    filteredReportDataList.add(data);
                }
            }
        }

        List<SalesReportData> tempList = getFilteredSalesReportDataByMaxRow(maxRow, reportDataList);
        filteredReportDataList = tempList;

        List<String> headers = getHeadersByIsNatTrade(isNatTrade);

        SalesActivityReport report = this.generateReport(headers, reportDataList);

        EcmService ecmService = new EcmService();
        ecmService.uploadDocument(report.toXml());

    }

    private List<SalesReportData> getFilteredSalesReportDataByMaxRow(int maxRow, List<SalesReportData> reportDataList) {
        List<SalesReportData> tempList = new ArrayList<SalesReportData>();
        for (int i = 0; i < reportDataList.size() || i < maxRow; i++) {
            tempList.add(reportDataList.get(i));
        }
        return tempList;
    }

    protected List<String> getHeadersByIsNatTrade(boolean isNatTrade) {
        List<String> headers = null;
        if (isNatTrade) {
            headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
        } else {
            headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
        }
        return headers;
    }

    protected List<SalesReportData> getSalesReportDataBySales(Sales sales) {
        return salesReportDao.getReportData(sales);
    }

    protected boolean isSalesEffective(Sales sales) {
        Date today = new Date();
        if (today.after(sales.getEffectiveTo())
                || today.before(sales.getEffectiveFrom())) {
            return true;
        }
        return false;
    }

    protected Sales getSalesBySaleId(String salesId) {
        return salesDao.getSalesBySalesId(salesId);
    }

    private SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
        // TODO Auto-generated method stub
        return null;
    }

}