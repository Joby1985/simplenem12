// Copyright Red Energy Limited 2017

package nem12.simplenem12.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a meter volume with quality, values should come from RecordType
 * 300
 */
public class MeterVolume implements NEM12Entry {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private BigDecimal volume;
    private Quality quality;
    private LocalDate date;

    public MeterVolume(String strDate, BigDecimal volume, Quality quality) {
        this.date = LocalDate.parse(strDate, dateFormatter);
        this.volume = volume;
        this.quality = quality;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MeterVolume that = (MeterVolume) o;
        return Objects.equals(getVolume(), that.getVolume()) && getQuality() == that.getQuality()
                && Objects.equals(getDate(), that.getDate());
    }

    public int hashCode() {
        return Objects.hash(getVolume(), getQuality(), getDate());
    }

	@Override
	public boolean isMeterReadEntry() {
		return false;
	}
}
