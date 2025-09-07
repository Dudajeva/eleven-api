package org.adzc.elevenapi.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Date/Time utility methods that complement Apache Commons DateUtils.
 *
 * 统一对外使用 java.util.Date；内部用 java.time 计算并在边界转换（时区：Asia/Shanghai）。
 */
public final class DateUtil {

    private DateUtil() {}

    /* ------------------------------ 基础差值计算 ------------------------------ */

    /** 计算两个 Temporal 之间的绝对 Duration（自动按最细粒度推断，推荐用于 Instant/Offset/Zoned/LocalDateTime）。 */
    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        Objects.requireNonNull(startInclusive, "startInclusive");
        Objects.requireNonNull(endExclusive, "endExclusive");
        // 优先使用 Instant 保障跨时区一致性
        if (startInclusive instanceof Instant && endExclusive instanceof Instant) {
            return Duration.between((Instant) startInclusive, (Instant) endExclusive).abs();
        }
        // 若包含时区信息，统一转换为 Instant
        if (hasZone(startInclusive) && hasZone(endExclusive)) {
            Instant s = toInstant(startInclusive);
            Instant e = toInstant(endExclusive);
            return Duration.between(s, e).abs();
        }
        // 对 LocalDateTime：需要一个 Zone 才有绝对时长语义，默认使用系统区
        if (startInclusive instanceof LocalDateTime && endExclusive instanceof LocalDateTime) {
            ZonedDateTime s = ((LocalDateTime) startInclusive).atZone(ZoneId.systemDefault());
            ZonedDateTime e = ((LocalDateTime) endExclusive).atZone(ZoneId.systemDefault());
            return Duration.between(s, e).abs();
        }
        // 对 LocalDate：使用 Period 不含“时长”，但可退化为天的 Duration
        if (startInclusive instanceof LocalDate && endExclusive instanceof LocalDate) {
            long days = ChronoUnit.DAYS.between((LocalDate) startInclusive, (LocalDate) endExclusive);
            return Duration.ofDays(Math.abs(days));
        }
        // 回退：按纳秒级单位计算
        long nanos = Math.abs(startInclusive.until(endExclusive, ChronoUnit.NANOS));
        return Duration.ofNanos(nanos);
    }

    /** 以指定单位返回差值（可用于任何 Temporal），结果为绝对值。 */
    public static long diff(Temporal start, Temporal end, ChronoUnit unit) {
        Objects.requireNonNull(unit, "unit");
        return Math.abs(unit.between(start, end));
    }

    /** 返回带符号的差值（end - start），便于判断正负。 */
    public static long diffSigned(Temporal start, Temporal end, ChronoUnit unit) {
        Objects.requireNonNull(unit, "unit");
        return unit.between(start, end);
    }

    /** 以 Period 表示两个“日期”的差（年-月-日），对外使用 java.util.Date。 */
    public static Period period(Date start, Date end) {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        LocalDate s = toLocalDate(start);
        LocalDate e = toLocalDate(end);
        return Period.between(s, e);
    }

    /** 人类可读的时长描述（中文）：例如 2天3小时5分10秒；为 0 时返回“0秒”。 */
    public static String humanize(Duration duration) {
        Objects.requireNonNull(duration, "duration");
        long seconds = Math.abs(duration.getSeconds());
        long days = seconds / (24 * 3600); seconds %= 24 * 3600;
        long hours = seconds / 3600;       seconds %= 3600;
        long minutes = seconds / 60;       seconds %= 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0)    sb.append(days).append("天");
        if (hours > 0)   sb.append(hours).append("小时");
        if (minutes > 0) sb.append(minutes).append("分");
        if (seconds > 0 || sb.length() == 0) sb.append(seconds).append("秒");
        return sb.toString();
    }

    /* ------------------------------ 工作日/商用日 ------------------------------ */

    private static final Set<DayOfWeek> WEEKEND = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    private static final ZoneId ZONE = ZoneId.of("Asia/Shanghai");

    /** 判断是否为工作日（周一至周五且不在节假日集合中）。holidays 为“日期型”集合（忽略时分秒）。 */
    public static boolean isBusinessDay(Date date, Set<Date> holidays) {
        Objects.requireNonNull(date, "date");
        LocalDate d = toLocalDate(date);
        boolean weekend = WEEKEND.contains(d.getDayOfWeek());
        boolean holiday = holidays != null && toLocalDates(holidays).contains(d);
        return !weekend && !holiday;
    }

    /** 计算两个日期之间的工作日数量（startInclusive, endExclusive）。 */
    public static long businessDaysBetween(Date startInclusive, Date endExclusive, Set<Date> holidays) {
        Objects.requireNonNull(startInclusive, "startInclusive");
        Objects.requireNonNull(endExclusive, "endExclusive");
        LocalDate s = toLocalDate(startInclusive);
        LocalDate e = toLocalDate(endExclusive);
        Set<LocalDate> hol = toLocalDates(holidays);

        long step = s.isBefore(e) ? 1 : -1;
        long count = 0;
        for (LocalDate d = s; !d.equals(e); d = d.plusDays(step)) {
            boolean weekend = WEEKEND.contains(d.getDayOfWeek());
            boolean holiday = hol != null && hol.contains(d);
            if (!weekend && !holiday) count++;
        }
        return Math.abs(count);
    }

    /** 增加/减少工作日（可为负），跳过周末与节假日。 */
    public static Date addBusinessDays(Date start, long bizDays, Set<Date> holidays) {
        Objects.requireNonNull(start, "start");
        LocalDate d = toLocalDate(start);
        Set<LocalDate> hol = toLocalDates(holidays);

        long remaining = Math.abs(bizDays);
        int dir = bizDays >= 0 ? 1 : -1;
        while (remaining > 0) {
            d = d.plusDays(dir);
            boolean weekend = WEEKEND.contains(d.getDayOfWeek());
            boolean holiday = hol != null && hol.contains(d);
            if (!weekend && !holiday) remaining--;
        }
        return toDate(d);
    }

    /** 下一个工作日（若当天不是工作日则推进到最近的下一个工作日）。 */
    public static Date nextBusinessDay(Date date, Set<Date> holidays) {
        Objects.requireNonNull(date, "date");
        LocalDate d = toLocalDate(date);
        Set<LocalDate> hol = toLocalDates(holidays);
        while (true) {
            boolean weekend = WEEKEND.contains(d.getDayOfWeek());
            boolean holiday = hol != null && hol.contains(d);
            if (!weekend && !holiday) return toDate(d);
            d = d.plusDays(1);
        }
    }

    /* ------------------------------ 取整/对齐（保留） ------------------------------ */

    /** 向下取整到给定间隔（例如 15 分钟）。 */
    public static ZonedDateTime roundDown(ZonedDateTime dt, Duration interval) {
        Objects.requireNonNull(dt, "dt");
        checkInterval(interval);
        long epoch = dt.toInstant().toEpochMilli();
        long ms = interval.toMillis();
        long snapped = (epoch / ms) * ms;
        return Instant.ofEpochMilli(snapped).atZone(dt.getZone());
    }

    /** 向上取整到给定间隔。 */
    public static ZonedDateTime roundUp(ZonedDateTime dt, Duration interval) {
        Objects.requireNonNull(dt, "dt");
        checkInterval(interval);
        long epoch = dt.toInstant().toEpochMilli();
        long ms = interval.toMillis();
        long snapped = ((epoch + ms - 1) / ms) * ms;
        return Instant.ofEpochMilli(snapped).atZone(dt.getZone());
    }

    /** 四舍五入到最近间隔。 */
    public static ZonedDateTime roundNearest(ZonedDateTime dt, Duration interval) {
        Objects.requireNonNull(dt, "dt");
        checkInterval(interval);
        long epoch = dt.toInstant().toEpochMilli();
        long ms = interval.toMillis();
        long snapped = ((epoch + ms / 2) / ms) * ms;
        return Instant.ofEpochMilli(snapped).atZone(dt.getZone());
    }

    private static void checkInterval(Duration interval) {
        Objects.requireNonNull(interval, "interval");
        if (interval.isZero() || interval.isNegative()) {
            throw new IllegalArgumentException("interval must be > 0");
        }
    }

    /* ------------------------------ 区间与重叠（保留） ------------------------------ */

    /** 区间是否重叠（[s1, e1) 与 [s2, e2)）。 */
    public static boolean isOverlapping(Instant s1, Instant e1, Instant s2, Instant e2) {
        validateRange(s1, e1); validateRange(s2, e2);
        return s1.isBefore(e2) && s2.isBefore(e1);
    }

    /** 返回两个区间的重叠时长（无重叠则为 Duration.ZERO）。 */
    public static Duration overlap(Instant s1, Instant e1, Instant s2, Instant e2) {
        validateRange(s1, e1); validateRange(s2, e2);
        Instant start = s1.isAfter(s2) ? s1 : s2;
        Instant end   = e1.isBefore(e2) ? e1 : e2;
        return end.isAfter(start) ? Duration.between(start, end) : Duration.ZERO;
    }

    /** 将目标区间 clamp 到边界区间内（如无交集，返回空 Optional）。 */
    public static Optional<Range<Instant>> clamp(Instant s, Instant e, Instant boundS, Instant boundE) {
        validateRange(s, e); validateRange(boundS, boundE);
        Instant ns = s.isBefore(boundS) ? boundS : s;
        Instant ne = e.isAfter(boundE)  ? boundE : e;
        return ne.isAfter(ns) ? Optional.of(new Range<>(ns, ne)) : Optional.empty();
    }

    public static final class Range<T> {
        public final T startInclusive;
        public final T endExclusive;
        public Range(T s, T e) { this.startInclusive = s; this.endExclusive = e; }
        @Override public String toString() { return "[" + startInclusive + "," + endExclusive + ")"; }
    }

    private static void validateRange(Instant s, Instant e) {
        Objects.requireNonNull(s, "start");
        Objects.requireNonNull(e, "end");
        if (!e.isAfter(s)) throw new IllegalArgumentException("end must be after start");
    }

    /* ------------------------------ 账期/季度/周序（Date 版） ------------------------------ */

    /** 获取给定日期所在季度的起始日期（当季第一天）。 */
    public static Date quarterStart(Date date) {
        Objects.requireNonNull(date, "date");
        LocalDate d = toLocalDate(date);
        int q = ((d.getMonthValue() - 1) / 3) * 3 + 1;
        return toDate(LocalDate.of(d.getYear(), q, 1));
    }

    /** 获取给定日期所在季度的结束日期（当季最后一天）。 */
    public static Date quarterEnd(Date date) {
        LocalDate start = toLocalDate(quarterStart(date));
        return toDate(start.plusMonths(3).minusDays(1));
    }

    /** 当前日期是本月的第几周（ISO 规则，周一为一周开始）。 */
    public static int weekOfMonth(Date date) {
        Objects.requireNonNull(date, "date");
        LocalDate d = toLocalDate(date);
        return d.get(ChronoField.ALIGNED_WEEK_OF_MONTH);
    }

    /** 当前日期是当年的第几周（ISO 规则）。 */
    public static int weekOfYear(Date date) {
        Objects.requireNonNull(date, "date");
        LocalDate d = toLocalDate(date);
        WeekFields wf = WeekFields.ISO;
        return d.get(wf.weekOfYear());
    }

    /* ------------------------------ 年龄/生日（Date 版） ------------------------------ */

    /** 计算某人在 onDate 当日的“整数年龄”（未过生日不加一）。 */
    public static int ageOn(Date birthDate, Date onDate) {
        Objects.requireNonNull(birthDate, "birthDate");
        Objects.requireNonNull(onDate, "onDate");
        LocalDate b = toLocalDate(birthDate);
        LocalDate o = toLocalDate(onDate);
        if (o.isBefore(b)) throw new IllegalArgumentException("onDate before birthDate");
        return Period.between(b, o).getYears();
    }

    /** 下一次生日日期（若当天就是生日，则返回下一年生日；闰年 2/29 在非闰年顺延至 2/28）。 */
    public static Date nextBirthday(Date birthDate, Date from) {
        Objects.requireNonNull(birthDate, "birthDate");
        Objects.requireNonNull(from, "from");
        LocalDate b = toLocalDate(birthDate);
        LocalDate f = toLocalDate(from);
        LocalDate candidate = b.withYear(f.getYear());
        if (!candidate.isAfter(f)) candidate = candidate.plusYears(1);
        if (candidate.getMonth() == Month.FEBRUARY && candidate.getDayOfMonth() == 29 &&
                !Year.isLeap(candidate.getYear())) {
            candidate = LocalDate.of(candidate.getYear(), Month.FEBRUARY, 28);
        }
        return toDate(candidate);
    }

    /* ------------------------------ 拆分/切片（保留） ------------------------------ */

    /** 将一个区间按“自然日”切分成多段 [start,end) 列表，常用于统计日级用量。 */
    public static List<Range<ZonedDateTime>> splitByDay(ZonedDateTime start, ZonedDateTime end) {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        if (!end.isAfter(start)) return List.of();

        List<Range<ZonedDateTime>> out = new ArrayList<>();
        ZonedDateTime cursor = start;
        while (cursor.isBefore(end)) {
            ZonedDateTime dayEnd = cursor.toLocalDate().plusDays(1).atStartOfDay(cursor.getZone());
            ZonedDateTime segmentEnd = dayEnd.isBefore(end) ? dayEnd : end;
            out.add(new Range<>(cursor, segmentEnd));
            cursor = segmentEnd;
        }
        return out;
    }

    /* ------------------------------ 解析/格式化（轻便，保留 LocalDateTime/Zoned） ------------------------------ */

    /** 尝试用一组常见格式解析本地日期时间（不含时区）。 */
    public static Optional<LocalDateTime> tryParseLocalDateTime(String text, List<String> patterns) {
        Objects.requireNonNull(text, "text");
        if (patterns == null || patterns.isEmpty()) {
            patterns = List.of("yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy-MM-dd'T'HH:mm:ss");
        }
        for (String p : patterns) {
            try {
                return Optional.of(LocalDateTime.parse(text, DateTimeFormatter.ofPattern(p)));
            } catch (Exception ignore) {}
        }
        return Optional.empty();
    }

    /** 使用多个格式尝试解析为 ZonedDateTime，默认时区可指定。 */
    public static Optional<ZonedDateTime> tryParseZoned(String text, ZoneId defaultZone, List<String> patterns) {
        Objects.requireNonNull(text, "text");
        ZoneId zone = defaultZone != null ? defaultZone : ZoneId.systemDefault();
        if (patterns == null || patterns.isEmpty()) {
            patterns = List.of(
                    "yyyy-MM-dd HH:mm:ss VV", "yyyy-MM-dd HH:mm:ssXXX",
                    "yyyy/MM/dd HH:mm VV",    "yyyy-MM-dd'T'HH:mm:ssXXX",
                    "yyyy-MM-dd'T'HH:mm:ss"
            );
        }
        for (String p : patterns) {
            try {
                DateTimeFormatter f = DateTimeFormatter.ofPattern(p).withZone(zone);
                TemporalAccessor parsed = f.parseBest(text, ZonedDateTime::from, LocalDateTime::from);
                if (parsed instanceof ZonedDateTime) return Optional.of((ZonedDateTime) parsed);
                if (parsed instanceof LocalDateTime)
                    return Optional.of(((LocalDateTime) parsed).atZone(zone));
            } catch (Exception ignore) {}
        }
        return Optional.empty();
    }

    /* ------------------------------ 辅助 ------------------------------ */

    private static boolean hasZone(Temporal t) {
        return t instanceof ZonedDateTime || t instanceof OffsetDateTime || t instanceof Instant;
    }

    private static Instant toInstant(Temporal t) {
        if (t instanceof Instant) return (Instant) t;
        if (t instanceof ZonedDateTime) return ((ZonedDateTime) t).toInstant();
        if (t instanceof OffsetDateTime) return ((OffsetDateTime) t).toInstant();
        throw new IllegalArgumentException("Temporal lacks zone/offset: " + t.getClass());
    }

    /** Date -> LocalDate（Asia/Shanghai） */
    private static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZONE).toLocalDate();
    }

    /** LocalDate -> Date（当天 00:00，Asia/Shanghai） */
    private static Date toDate(LocalDate d) {
        return Date.from(d.atStartOfDay(ZONE).toInstant());
    }

    /** Set<Date> -> Set<LocalDate>（按 Asia/Shanghai 截断到日期） */
    private static Set<LocalDate> toLocalDates(Set<Date> dates) {
        if (dates == null) return null;
        return dates.stream().filter(Objects::nonNull).map(DateUtil::toLocalDate).collect(Collectors.toSet());
    }

    /* ------------------------------ 批量工具示例（保留） ------------------------------ */

    /** 将一组区间合并（同类型 Instant 区间），返回去重叠后的并集。 */
    public static List<Range<Instant>> mergeIntervals(List<Range<Instant>> intervals) {
        if (intervals == null || intervals.isEmpty()) return List.of();
        List<Range<Instant>> sorted = intervals.stream()
                .sorted(Comparator.comparing(r -> r.startInclusive))
                .collect(Collectors.toList());
        List<Range<Instant>> out = new ArrayList<>();
        Range<Instant> cur = sorted.get(0);
        for (int i = 1; i < sorted.size(); i++) {
            Range<Instant> r = sorted.get(i);
            if (!r.startInclusive.isAfter(cur.endExclusive)) {
                Instant newEnd = r.endExclusive.isAfter(cur.endExclusive) ? r.endExclusive : cur.endExclusive;
                cur = new Range<>(cur.startInclusive, newEnd);
            } else {
                out.add(cur);
                cur = r;
            }
        }
        out.add(cur);
        return out;
    }
}
