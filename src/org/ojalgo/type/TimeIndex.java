package org.ojalgo.type;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.ojalgo.access.IndexMapper;

public abstract class TimeIndex<T extends Comparable<? super T>> {

    public static final TimeIndex<Calendar> CALENDAR = new TimeIndex<Calendar>() {

        @Override
        public IndexMapper<Calendar> from(final Calendar reference) {
            return new IndexMapper<Calendar>() {

                public long toIndex(final Calendar key) {
                    return key.getTimeInMillis() - reference.getTimeInMillis();
                }

                public Calendar toKey(final long index) {
                    final long tmpTimeInMillis = index * +reference.getTimeInMillis();
                    final GregorianCalendar retVal = new GregorianCalendar();
                    retVal.setTimeInMillis(tmpTimeInMillis);
                    return retVal;
                }

            };
        }

        @Override
        public IndexMapper<Calendar> from(final Calendar reference, final CalendarDateDuration resolution) {
            return new IndexMapper<Calendar>() {

                public long toIndex(final Calendar key) {
                    return (key.getTimeInMillis() - reference.getTimeInMillis()) / resolution.toDurationInMillis();
                }

                public Calendar toKey(final long index) {
                    final long tmpTimeInMillis = (index * resolution.toDurationInMillis()) + reference.getTimeInMillis();
                    final GregorianCalendar retVal = new GregorianCalendar();
                    retVal.setTimeInMillis(tmpTimeInMillis);
                    return retVal;
                }

            };
        }

        @Override
        public IndexMapper<Calendar> plain() {
            return new IndexMapper<Calendar>() {

                public long toIndex(final Calendar key) {
                    return key.getTimeInMillis();
                }

                public Calendar toKey(final long index) {
                    final GregorianCalendar retVal = new GregorianCalendar();
                    retVal.setTimeInMillis(index);
                    return retVal;
                }

            };
        }

    };

    public static final TimeIndex<CalendarDate> CALENDAR_DATE = new TimeIndex<CalendarDate>() {

        @Override
        public IndexMapper<CalendarDate> from(final CalendarDate reference) {
            return new IndexMapper<CalendarDate>() {

                public long toIndex(final CalendarDate key) {
                    return key.millis - reference.millis;
                }

                public CalendarDate toKey(final long index) {
                    return new CalendarDate(index + reference.millis);
                }

            };
        }

        @Override
        public IndexMapper<CalendarDate> from(final CalendarDate reference, final CalendarDateDuration resolution) {
            return new IndexMapper<CalendarDate>() {

                public long toIndex(final CalendarDate key) {
                    return (key.millis - reference.millis) / resolution.toDurationInMillis();
                }

                public CalendarDate toKey(final long index) {
                    return new CalendarDate((index * resolution.toDurationInMillis()) + reference.millis);
                }

            };
        }

        @Override
        public IndexMapper<CalendarDate> plain() {
            return new IndexMapper<CalendarDate>() {

                public long toIndex(final CalendarDate key) {
                    return key.millis;
                }

                public CalendarDate toKey(final long index) {
                    return new CalendarDate(index);
                }

            };
        }

    };

    public static final TimeIndex<Date> DATE = new TimeIndex<Date>() {

        @Override
        public IndexMapper<Date> from(final Date reference) {
            return new IndexMapper<Date>() {

                public long toIndex(final Date key) {
                    return key.getTime() - reference.getTime();
                }

                public Date toKey(final long index) {
                    return new Date(index + reference.getTime());
                }

            };
        }

        @Override
        public IndexMapper<Date> from(final Date reference, final CalendarDateDuration resolution) {
            return new IndexMapper<Date>() {

                public long toIndex(final Date key) {
                    return (key.getTime() - reference.getTime()) / resolution.toDurationInMillis();
                }

                public Date toKey(final long index) {
                    return new Date((index * resolution.toDurationInMillis()) + reference.getTime());
                }

            };
        }

        @Override
        public IndexMapper<Date> plain() {
            return new IndexMapper<Date>() {

                public long toIndex(final Date key) {
                    return key.getTime();
                }

                public Date toKey(final long index) {
                    return new Date(index);
                }

            };
        }

    };

    public static final TimeIndex<Instant> INSTANT = new TimeIndex<Instant>() {

        @Override
        public IndexMapper<Instant> from(final Instant reference) {
            return new IndexMapper<Instant>() {

                public long toIndex(final Instant key) {
                    return reference.until(key, ChronoUnit.NANOS);
                }

                public Instant toKey(final long index) {
                    return reference.plus(index, ChronoUnit.NANOS);
                }

            };
        }

        @Override
        public IndexMapper<Instant> from(final Instant reference, final CalendarDateDuration resolution) {
            return new IndexMapper<Instant>() {

                private final long myResolution = resolution.toDurationInNanos();

                public long toIndex(final Instant key) {
                    return reference.until(key, ChronoUnit.NANOS) / myResolution;
                }

                public Instant toKey(final long index) {
                    return reference.plus(index * myResolution, ChronoUnit.NANOS);
                }

            };
        }

        @Override
        public IndexMapper<Instant> plain() {
            return new IndexMapper<Instant>() {

                public long toIndex(final Instant key) {
                    return key.toEpochMilli();
                }

                public Instant toKey(final long index) {
                    return Instant.ofEpochMilli(index);
                }

            };
        }

    };

    public static final TimeIndex<LocalDate> LOCAL_DATE = new TimeIndex<LocalDate>() {

        @Override
        public IndexMapper<LocalDate> from(final LocalDate reference) {
            return new IndexMapper<LocalDate>() {

                private final long myReference = reference.toEpochDay();

                public long toIndex(final LocalDate key) {
                    return key.toEpochDay() - myReference;
                }

                public LocalDate toKey(final long index) {
                    return LocalDate.ofEpochDay(myReference + index);
                }

            };
        }

        @Override
        public IndexMapper<LocalDate> from(final LocalDate reference, final CalendarDateDuration resolution) {
            return new IndexMapper<LocalDate>() {

                private final long myReference = reference.toEpochDay();
                private final long myResolution = resolution.toDurationInMillis();

                public long toIndex(final LocalDate key) {
                    return (key.toEpochDay() - myReference) / myResolution;
                }

                public LocalDate toKey(final long index) {
                    return LocalDate.ofEpochDay(myReference + (index * myResolution));
                }

            };
        }

        @Override
        public IndexMapper<LocalDate> plain() {
            return new IndexMapper<LocalDate>() {

                public long toIndex(final LocalDate key) {
                    return key.toEpochDay();
                }

                public LocalDate toKey(final long index) {
                    return LocalDate.ofEpochDay(index);
                }

            };
        }

    };

    public static final TimeIndex<LocalDateTime> LOCAL_DATE_TIME = new TimeIndex<LocalDateTime>() {

        @Override
        public IndexMapper<LocalDateTime> from(final LocalDateTime reference) {
            return new IndexMapper<LocalDateTime>() {

                private final long myReference = reference.toEpochSecond(ZoneOffset.UTC);

                public long toIndex(final LocalDateTime key) {
                    return key.toEpochSecond(ZoneOffset.UTC) - myReference;
                }

                public LocalDateTime toKey(final long index) {
                    return LocalDateTime.ofEpochSecond(myReference + index, 0, ZoneOffset.UTC);
                }

            };
        }

        @Override
        public IndexMapper<LocalDateTime> from(final LocalDateTime reference, final CalendarDateDuration resolution) {
            return new IndexMapper<LocalDateTime>() {

                private final long myReference = reference.toEpochSecond(ZoneOffset.UTC);
                private final long myResolution = resolution.toDurationInMillis();

                public long toIndex(final LocalDateTime key) {
                    return (key.toEpochSecond(ZoneOffset.UTC) - myReference) / myResolution;
                }

                public LocalDateTime toKey(final long index) {
                    return LocalDateTime.ofEpochSecond(myReference + (index * myResolution), 0, ZoneOffset.UTC);
                }

            };
        }

        @Override
        public IndexMapper<LocalDateTime> plain() {
            return new IndexMapper<LocalDateTime>() {

                public long toIndex(final LocalDateTime key) {
                    return key.toEpochSecond(ZoneOffset.UTC);
                }

                public LocalDateTime toKey(final long index) {
                    return LocalDateTime.ofEpochSecond(index, 0, ZoneOffset.UTC);
                }

            };
        }

    };

    public static final TimeIndex<LocalTime> LOCAL_TIME = new TimeIndex<LocalTime>() {

        @Override
        public IndexMapper<LocalTime> from(final LocalTime reference) {
            return new IndexMapper<LocalTime>() {

                final long myReference = reference.toNanoOfDay();

                public long toIndex(final LocalTime key) {
                    return key.toNanoOfDay() - myReference;
                }

                public LocalTime toKey(final long index) {
                    return LocalTime.ofNanoOfDay(myReference + index);
                }

            };
        }

        @Override
        public IndexMapper<LocalTime> from(final LocalTime reference, final CalendarDateDuration resolution) {
            return new IndexMapper<LocalTime>() {

                final long myReference = reference.toNanoOfDay();
                final long myResolution = resolution.toDurationInNanos();

                public long toIndex(final LocalTime key) {
                    return (key.toNanoOfDay() - myReference) / myResolution;
                }

                public LocalTime toKey(final long index) {
                    return LocalTime.ofNanoOfDay(myReference + (index * myResolution));
                }

            };
        }

        @Override
        public IndexMapper<LocalTime> plain() {
            return new IndexMapper<LocalTime>() {

                public long toIndex(final LocalTime key) {
                    return key.toNanoOfDay();
                }

                public LocalTime toKey(final long index) {
                    return LocalTime.ofNanoOfDay(index);
                }

            };
        }

    };

    public static final TimeIndex<Long> LONG = new TimeIndex<Long>() {

        @Override
        public IndexMapper<Long> from(final Long reference) {
            return new IndexMapper<Long>() {

                public long toIndex(final Long key) {
                    return key - reference;
                }

                public Long toKey(final long index) {
                    return index + reference;
                }

            };
        }

        @Override
        public IndexMapper<Long> from(final Long reference, final CalendarDateDuration resolution) {
            return new IndexMapper<Long>() {

                public long toIndex(final Long key) {
                    return (key - reference) / resolution.toDurationInMillis();
                }

                public Long toKey(final long index) {
                    return (index * resolution.toDurationInMillis()) + reference;
                }

            };
        }

        @Override
        public IndexMapper<Long> plain() {
            return new IndexMapper<Long>() {

                public long toIndex(final Long key) {
                    return key;
                }

                public Long toKey(final long index) {
                    return index;
                }

            };
        }

    };

    public static final TimeIndex<OffsetDateTime> OFFSET_DATE_TIME = new TimeIndex<OffsetDateTime>() {

        @Override
        public IndexMapper<OffsetDateTime> from(final OffsetDateTime reference) {
            return new IndexMapper<OffsetDateTime>() {

                private final IndexMapper<Instant> myDelegate = INSTANT.from(reference.toInstant());
                private final ZoneOffset myOffset = reference.getOffset();

                public long toIndex(final OffsetDateTime key) {
                    return myDelegate.toIndex(key.toInstant());
                }

                public OffsetDateTime toKey(final long index) {
                    final Instant tmpInstant = myDelegate.toKey(index);
                    if (myOffset != null) {
                        return OffsetDateTime.ofInstant(tmpInstant, myOffset);
                    } else {
                        return OffsetDateTime.ofInstant(tmpInstant, ZoneOffset.UTC);
                    }
                }
            };
        }

        @Override
        public IndexMapper<OffsetDateTime> from(final OffsetDateTime reference, final CalendarDateDuration resolution) {
            return new IndexMapper<OffsetDateTime>() {

                private final IndexMapper<Instant> myDelegate = INSTANT.from(reference.toInstant(), resolution);
                private final ZoneOffset myOffset = reference.getOffset();

                public long toIndex(final OffsetDateTime key) {
                    return myDelegate.toIndex(key.toInstant());
                }

                public OffsetDateTime toKey(final long index) {
                    final Instant tmpInstant = myDelegate.toKey(index);
                    if (myOffset != null) {
                        return OffsetDateTime.ofInstant(tmpInstant, myOffset);
                    } else {
                        return OffsetDateTime.ofInstant(tmpInstant, ZoneOffset.UTC);
                    }
                }
            };
        }

        @Override
        public IndexMapper<OffsetDateTime> plain() {
            return new IndexMapper<OffsetDateTime>() {

                private final IndexMapper<Instant> myDelegate = INSTANT.plain();
                private transient ZoneOffset myOffset = null;

                public long toIndex(final OffsetDateTime key) {
                    myOffset = key.getOffset();
                    return myDelegate.toIndex(key.toInstant());
                }

                public OffsetDateTime toKey(final long index) {
                    final Instant tmpInstant = myDelegate.toKey(index);
                    if (myOffset != null) {
                        return OffsetDateTime.ofInstant(tmpInstant, myOffset);
                    } else {
                        return OffsetDateTime.ofInstant(tmpInstant, ZoneOffset.UTC);
                    }
                }
            };
        }

    };

    public static final TimeIndex<ZonedDateTime> ZONED_DATE_TIME = new TimeIndex<ZonedDateTime>() {

        @Override
        public IndexMapper<ZonedDateTime> from(final ZonedDateTime reference) {
            return new IndexMapper<ZonedDateTime>() {

                private final IndexMapper<Instant> myDelegate = INSTANT.from(reference.toInstant());
                private final ZoneId myZone = reference.getZone();

                public long toIndex(final ZonedDateTime key) {
                    return myDelegate.toIndex(key.toInstant());
                }

                public ZonedDateTime toKey(final long index) {
                    final Instant tmpInstant = myDelegate.toKey(index);
                    if (myZone != null) {
                        return ZonedDateTime.ofInstant(tmpInstant, myZone);
                    } else {
                        return ZonedDateTime.ofInstant(tmpInstant, ZoneOffset.UTC);
                    }
                }
            };
        }

        @Override
        public IndexMapper<ZonedDateTime> from(final ZonedDateTime reference, final CalendarDateDuration resolution) {
            return new IndexMapper<ZonedDateTime>() {

                private final IndexMapper<Instant> myDelegate = INSTANT.from(reference.toInstant(), resolution);
                private final ZoneId myZone = reference.getZone();

                public long toIndex(final ZonedDateTime key) {
                    return myDelegate.toIndex(key.toInstant());
                }

                public ZonedDateTime toKey(final long index) {
                    final Instant tmpInstant = myDelegate.toKey(index);
                    if (myZone != null) {
                        return ZonedDateTime.ofInstant(tmpInstant, myZone);
                    } else {
                        return ZonedDateTime.ofInstant(tmpInstant, ZoneOffset.UTC);
                    }
                }
            };
        }

        @Override
        public IndexMapper<ZonedDateTime> plain() {
            return new IndexMapper<ZonedDateTime>() {

                private final IndexMapper<Instant> myDelegate = INSTANT.plain();
                private transient ZoneId myZone = null;

                public long toIndex(final ZonedDateTime key) {
                    myZone = key.getZone();
                    return myDelegate.toIndex(key.toInstant());
                }

                public ZonedDateTime toKey(final long index) {
                    final Instant tmpInstant = myDelegate.toKey(index);
                    if (myZone != null) {
                        return ZonedDateTime.ofInstant(tmpInstant, myZone);
                    } else {
                        return ZonedDateTime.ofInstant(tmpInstant, ZoneOffset.UTC);
                    }
                }
            };
        }

    };

    public abstract IndexMapper<T> from(final T reference);

    public abstract IndexMapper<T> from(final T reference, final CalendarDateDuration resolution);

    public abstract IndexMapper<T> plain();

}