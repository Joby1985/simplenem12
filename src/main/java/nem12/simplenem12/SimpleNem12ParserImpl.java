package nem12.simplenem12;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import nem12.simplenem12.exceptions.NEM12ReadingException;
import nem12.simplenem12.model.EnergyUnit;
import nem12.simplenem12.model.MeterRead;
import nem12.simplenem12.model.MeterVolume;
import nem12.simplenem12.model.NEM12Entry;
import nem12.simplenem12.model.Quality;

public class SimpleNem12ParserImpl implements SimpleNem12Parser {
    private static final String NEM12_FILE_START = "100";
    private static final String NEM12_FILE_END = "900";
    private static final String NEM12_MTR_RD_BLK_START = "200";
    private static final String NEM12_MTR_RD_ELEMENT = "300";

    @Override
    public Collection<MeterRead> parseSimpleNem12(File simpleNem12File) throws NEM12ReadingException {
        List<MeterRead> meterReads = new ArrayList<>();
        try (Scanner sc = new Scanner(simpleNem12File)) {
            List<String> readNEM12Data = validateAndGetNEM12Data(sc);
            int rowsOfData = readNEM12Data.size();
            if (rowsOfData > 0) {
                MeterRead lastReadMeter = null;
                for (String data : readNEM12Data) {
                    NEM12Entry nem12Entry = prepareNEM12Entry(data);
                    if (nem12Entry != null) {
                        if (nem12Entry instanceof MeterRead) {
                            lastReadMeter = (MeterRead) nem12Entry;
                            meterReads.add(lastReadMeter);
                        } else {
                            MeterVolume volume = (MeterVolume) nem12Entry;
                            lastReadMeter.appendVolume(volume.getDate(), volume);
                        }
                    }
                }
            }
        } catch (FileNotFoundException fnf) {
            System.out.println("File " + simpleNem12File.getName() + " could not be found.");
        }
        return meterReads;
    }

    /**
     * Process a NEM12 line as either 200 or 300 record - NEM12Entry
     * 
     * @param readNEM12Data
     * @return
     */
    private NEM12Entry prepareNEM12Entry(String readNEM12Data) {
        if (readNEM12Data.startsWith(NEM12_FILE_START) || readNEM12Data.startsWith(NEM12_FILE_END)) {
            return null;
        }
        String[] read = readNEM12Data.split(",");
        switch (read[0]) {
        case NEM12_MTR_RD_BLK_START:
            return new MeterRead(read[1], EnergyUnit.valueOf(read[2]));
        case NEM12_MTR_RD_ELEMENT:
            return new MeterVolume(read[1], new BigDecimal(read[2]), Quality.valueOf(read[3]));
        }
        return null;
    }

    /**
     * Validate the NEM12 file - start with 100, end with 900; and then return
     * individual lines as a list
     * 
     * @param sc
     * @return
     * @throws NEM12ReadingException
     */
    private List<String> validateAndGetNEM12Data(Scanner sc) throws NEM12ReadingException {
        sc.useDelimiter("\n");
        String line = null;
        boolean nem12Started = false;
        List<String> readLines = new ArrayList<>();
        while (sc.hasNext()) {
            line = sc.next();
            if (!line.isEmpty()) {
                if (!nem12Started) {
                    if (!line.equals(NEM12_FILE_START)) {
                        throw new NEM12ReadingException(
                                "Invalid NEM12 file. NEM12 must start with " + NEM12_FILE_START);
                    } else {
                        nem12Started = true;
                    }
                }
                readLines.add(line);
            }
        }
        if (nem12Started && !line.equals(NEM12_FILE_END)) {
            throw new NEM12ReadingException("Invalid NEM12 file. NEM12 must end with " + NEM12_FILE_END);
        }
        return readLines;
    }

}
