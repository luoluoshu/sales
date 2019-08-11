package sales;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SalesAppTest {

    @Mock(name = "salesDao")
    SalesDao salesDao;
    @Mock(name = "salesReportDao")
    SalesReportDao salesReportDao;
    @InjectMocks
    SalesApp salesApp = new SalesApp();

    @Test
    public void testGenerateReport() {

        SalesApp salesApp = new SalesApp();
    }

    @Test
    public void should_return_sales_when_call_getSalesBySaleId_given_DUMMY_salesId() {
        String salesId = "DUMMY";
        Sales sales1 = new Sales();
        sales1.setSupervisor(true);
        when(salesDao.getSalesBySalesId(salesId)).thenReturn(sales1);
        Sales sales = salesApp.getSalesBySaleId(salesId);
        assertTrue(sales.isSupervisor());

    }

    @Test
    public void should_return_true_when_call_isSalesEffective_given_the_last_day_and_the_next_day_from_now() {
        Sales mockSales = mock(Sales.class);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -1);
        Date startDate = startCalendar.getTime();

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DATE, 1);
        Date endDate = endCalendar.getTime();

        when(mockSales.getEffectiveFrom()).thenReturn(endDate);
        when(mockSales.getEffectiveTo()).thenReturn(startDate);

        boolean salesEffective = salesApp.isSalesEffective(mockSales);

        assertTrue(salesEffective);

    }

    @Test
    public void should_return_salesReportDatas_when_call_getSalesReportDataBySales_given_sales() {
        Sales sales = mock(Sales.class);

        SalesReportData salesReportData = mock(SalesReportData.class);
        when(salesReportData.isConfidential()).thenReturn(true);

        ArrayList<SalesReportData> salesReportDatas = new ArrayList<>();
        salesReportDatas.add(salesReportData);
        when(salesReportDao.getReportData(sales)).thenReturn(salesReportDatas);

        List<SalesReportData> salesReportDataBySales = salesApp.getSalesReportDataBySales(sales);

        assertTrue(salesReportDataBySales.get(0).isConfidential());

    }

    @Test
    public void should_return_specifyHeadersStrs_when_call_getHeadersByIsNatTrade_given_true() {
        boolean isNatTrade = true;
        List<String> headers = salesApp.getHeadersByIsNatTrade(isNatTrade);
        assertEquals("Time", headers.get(3));
    }

    @Test
    public void should_return_specifyHeadersStrs_when_call_getHeadersByIsNatTrade_given_false() {
        boolean isNatTrade = false;
        List<String> headers = salesApp.getHeadersByIsNatTrade(isNatTrade);
        assertEquals("Local Time", headers.get(3));
    }
}