package nem12.simplenem12;


import java.io.File;
import java.math.BigDecimal;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import nem12.simplenem12.exceptions.NEM12ReadingException;
import nem12.simplenem12.model.MeterRead;

public class SimpleNem12ParserTest {

    private SimpleNem12ParserImpl implementation;
    @Rule
    public ExpectedException  exceptionRule = ExpectedException.none();

    @Before
    public void setup() {
        implementation = new SimpleNem12ParserImpl();
    }

    @Test
    public void testParseSimpleNem12() throws Exception {
    	String fileName = "src/test/resources/SimpleNem12.csv";
        File simpleNem12File = new File(fileName);

        Collection<MeterRead> meterReads = implementation.parseSimpleNem12(simpleNem12File);

        MeterRead read6123456789 = meterReads.stream().filter(mr -> mr.getNmi().equals("6123456789")).findFirst().get();
        Assert.assertEquals(new BigDecimal("-36.84"), read6123456789.getTotalVolume());
        System.out.println(String.format("Total volume for NMI 6123456789 is %f", read6123456789.getTotalVolume())); // Should
                                                                                                                     // be
                                                                                                                     // -36.84

        MeterRead read6987654321 = meterReads.stream().filter(mr -> mr.getNmi().equals("6987654321")).findFirst().get();
        Assert.assertEquals(new BigDecimal("14.33"), read6987654321.getTotalVolume());
        System.out.println(String.format("Total volume for NMI 6987654321 is %f", read6987654321.getTotalVolume())); // Should
                
    }
    
    @Test
    public void testParseSimpleNem12InvalidFile() throws Exception {
    	exceptionRule.expect(NEM12ReadingException.class);
    	String fileName = "src/test/resources/SimpleNem12Invalid.csv";
        File simpleNem12File = new File(fileName);

        implementation.parseSimpleNem12(simpleNem12File);
    }
}
