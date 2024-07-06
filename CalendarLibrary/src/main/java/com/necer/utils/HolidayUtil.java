package com.necer.utils;

import android.text.TextUtils;

import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by necer on 2018/11/16.
 */
public class HolidayUtil {

    /**
     * 假日
     *
     * @param solarYear
     * @param solarMonth
     * @param solarDay
     * @return
     */
    public static String getHoliday(int lunarYear, int lunarMonth, int lunarDay, int solarYear, int solarMonth, int solarDay) {
        boolean isLeap = LunarUtil.isLeepMonth(solarYear, solarMonth, solarDay);
        String message = "";
        if (solarMonth == 1) {
            if (solarDay == 1 && solarYear >= 1949) {
                message = "元旦";
            }
        } else if (lunarMonth == 1 && lunarDay == 1 && !isLeap) {
            message = "春节";
        } else if (solarMonth == 4) {
            if (solarYear == 2000 && solarDay == 5) {

            } else if (solarDay >= 4 && solarDay <= 6) {
                int compare;
                if (solarYear <= 1999) {
                    compare = (int) ((double) (solarYear - 1900) * 0.2422D + 5.59D - (double) ((solarYear - 1900) / 4));
                    if (compare == solarDay) {
                        message = "清明";
                    }
                } else {
                    compare = (int) ((double) (solarYear - 2000) * 0.2422D + 4.81D - (double) ((solarYear - 2000) / 4));
                    if (compare == solarDay) {
                        message = "清明";
                    }
                }
            }
        } else if (solarMonth == 5) {
            if (solarDay == 1) {
                if (solarYear >= 1949) {
                    message = "劳动节";
                }
            }
        } else if (solarMonth == 10) {
            if (solarDay == 1) {
                if (solarYear >= 1949) {
                    message = "国庆节";
                }
            }
        } else if (lunarMonth == 8 && lunarDay == 15 && !isLeap) {
            message = "中秋节";
        }

        return message;
    }


    /**
     * 其他节日
     *
     * @return
     */
    public static String getSolarHoliday(int solarYear, int solarMonth, int solarDay) {
        String message = "";

        if (HolidayUtil.EasterDay.contains(solarYear + "-" + solarMonth + "-" + solarDay)) {
            message = "复活节";
        }

        if (solarMonth == 1) {
            if (solarDay == 1 && solarYear >= 1949) {
                message = "元旦";
            }
        } else if (solarMonth == 2) {
            if (solarDay == 2) {
                if (solarYear >= 1997) {
                    message = "湿地日";
                }
            } else if (solarDay == 14) {
                message = "情人节";
            }
        } else if (solarMonth == 3) {
            if (solarDay == 3) {
                if (solarYear >= 1999) {
                    message = "爱耳日";
                }
            } else if (solarDay == 8) {
                if (solarYear >= 1909) {
                    message = "妇女节";
                }
            } else if (solarDay == 12) {
                if (solarYear >= 1979) {
                    message = "植树节";
                }
            } else if (solarDay == 15 && solarYear >= 1983) {
                message = "消费者日";
            } else if (solarDay == 22 && solarYear >= 1993) {
                message = "世界水日";
            } else if (solarDay == 23 && solarYear >= 1961) {
                message = "气象日";
            }
        } else if (solarMonth == 4) {
            if (solarDay == 1) {
                if (solarYear >= 1983) {
                    message = "愚人节";
                }
            } else if (solarDay >= 4 && solarDay <= 6) {
                int compare;
                if (solarYear == 2000 && solarDay == 5) {

                } else if (solarYear <= 1999) {
                    compare = (int) ((double) (solarYear - 1900) * 0.2422D + 5.59D - (double) ((solarYear - 1900) / 4));
                    if (compare == solarDay) {
                        message = "清明";
                    }
                } else {
                    compare = (int) ((double) (solarYear - 2000) * 0.2422D + 4.81D - (double) ((solarYear - 2000) / 4));
                    if (compare == solarDay) {
                        message = "清明";
                    }
                }
            } else if (solarDay == 22 && solarYear >= 1970) {
                message = "地球日";
            } else if (solarDay == 26 && solarYear >= 2001) {
                message = "产权日";
            }
        } else if (solarMonth == 5) {
            if (solarDay == 1) {
                if (solarYear >= 1949) {
                    message = "劳动节";
                }
            } else if (solarDay == 4) {
                if (solarYear >= 1919) {
                    message = "青年节";
                }
            } else if (solarDay == 8) {
                if (solarYear >= 1948) {
                    message = "微笑日";
                }
            } else if (solarDay == 12) {
                if (solarYear >= 1912) {
                    message = "护士节";
                }
            } else if (solarDay == 18) {
                if (solarYear >= 1977) {
                    message = "博物馆日";
                }
            } else if (solarDay == 19) {
                if (solarYear >= 2001) {
                    message = "旅游日";
                }
            } else if (solarDay == 31 && solarYear >= 1989) {
                message = "无烟日";
            }

            if (solarDay >= 8 && solarDay <= 14 && !TextUtils.isEmpty(getMotherOrFatherDay(solarYear, solarMonth, solarDay))) {
                message = getMotherOrFatherDay(solarYear, solarMonth, solarDay);
            }
        } else if (solarMonth == 6) {
            if (solarDay == 1) {
                if (solarYear >= 1949) {
                    message = "儿童节";
                }
            } else if (solarDay == 23 && solarYear >= 1948) {
                message = "奥林匹克";
            } else if (solarDay == 6) {
                if (solarYear >= 1996) {
                    message = "爱眼日";
                }
            } else if (solarDay == 5) {
                if (solarYear >= 1972) {
                    message = "环保日";
                }
            }

            if (solarDay >= 15 && solarDay <= 21) {
                message = getMotherOrFatherDay(solarYear, solarMonth, solarDay);
            }
        } else if (solarMonth == 7) {
            if (solarDay == 1) {
                if (solarYear >= 1921) {
                    message = "建党节";
                }
            } else if (solarDay == 7 && solarYear >= 1937) {
                message = "七七事变";
            } else if (solarDay == 11 && solarYear >= 2005) {
                message = "航海日";
            }
        } else if (solarMonth == 8) {
            if (solarDay == 1 && solarYear >= 1949) {
                message = "建军节";
            } else if (solarDay == 15 && solarYear >= 1945) {
                message = "日本投降";
            }
        } else if (solarMonth == 9) {
            if (solarDay == 3) {
                if (solarYear >= 1945) {
                    message = "抗战胜利";
                }
            } else if (solarDay == 10) {
                if (solarYear >= 1985) {
                    message = "教师节";
                }
            } else if (solarDay == 18) {
                if (solarYear >= 1931) {
                    message = "九一八";
                }
            } else if (solarDay == 28) {
                message = "孔子诞辰";
            } else if (solarDay == 30) {
                if (solarYear >= 2014) {
                    message = "烈士纪念";
                }
            } else if (solarDay == 20) {
                if (solarYear >= 1989) {
                    message = "爱牙日";
                }
            }
        } else if (solarMonth == 10) {
            if (solarDay == 1) {
                if (solarYear >= 1949) {
                    message = "国庆节";
                }
            } else if (solarDay == 10) {
                if (solarYear >= 1912) {
                    message = "辛亥革命";
                }
            } else if (solarDay == 13) {
                if (solarYear >= 1950) {
                    message = "保健日";
                }

            } else if (solarDay == 16) {
                if (solarYear >= 1979) {
                    message = "粮食日";
                }

            } else if (solarDay == 24) {
                if (solarYear >= 1947) {
                    message = "联合国日";
                }

            } else if (solarDay == 31) {
                message = "万圣夜";
            }
        } else if (solarMonth == 11) {
            if (solarDay == 11 && solarYear >= 2015) {
                message = "光棍节";
            } else if (solarDay == 8 && solarYear >= 2000) {
                message = "记者日";
            } else if (solarDay == 17 && solarYear >= 1946) {
                message = "大学生节";
            }

            if (solarDay >= 20 && !TextUtils.isEmpty(getThanksGivingDay(solarYear, solarMonth, solarDay))) {
                message = getThanksGivingDay(solarYear, solarMonth, solarDay);
            }
        } else if (solarMonth == 12) {
            if (solarDay == 1) {
                if (solarYear >= 1988) {
                    message = "艾滋病日";
                }
            } else if (solarDay == 10) {
                if (solarYear >= 1948) {
                    message = "人权日";
                }
            } else if (solarDay == 13) {
                if (solarYear >= 1937) {
                    message = "公祭日";
                }
            } else if (solarDay == 20) {
                if (solarYear >= 1999) {
                    message = "澳门回归";
                }
            } else if (solarDay == 24) {
                message = "平安夜";
            } else if (solarDay == 25) {
                message = "圣诞节";
            }
        }


        return message;
    }


    /**
     * 传统节日
     *
     * @param lunarYear
     * @param lunarMonth
     * @param lunarDay
     * @param solarYear
     * @param solarMonth
     * @param solarDay
     * @param solarTerm
     * @return
     */
    public static String getLunarHoliday(int lunarYear, int lunarMonth, int lunarDay, int solarYear, int solarMonth, int solarDay, String solarTerm) {

        boolean isLeap = LunarUtil.isLeepMonth(solarYear, solarMonth, solarDay);

        String message = "";
        if (lunarMonth == 1 && lunarDay == 1 && !isLeap) {
            message = "春节";
        } else if (lunarMonth == 1 && lunarDay == 15 && !isLeap) {
            message = "元宵节";
        } else if (lunarMonth == 2 && lunarDay == 2 && !isLeap) {
            message = "龙抬头";
        } else if (lunarMonth == 3 && lunarDay == 3 && !isLeap) {
            message = "上巳节";
        } else if (lunarMonth == 4 && lunarDay == 8 && !isLeap) {
            message = "佛诞节";
        } else if (lunarMonth == 5 && lunarDay == 5 && !isLeap) {
            message = "端午节";
        } else if (lunarMonth == 7 && lunarDay == 7 && !isLeap) {
            message = "七夕";
        } else if (lunarMonth == 7 && lunarDay == 15 && !isLeap) {
            message = "中元节";
        } else if (lunarMonth == 8 && lunarDay == 15 && !isLeap) {
            message = "中秋节";
        } else if (lunarMonth == 9 && lunarDay == 9 && !isLeap) {
            message = "重阳节";
        } else if (lunarMonth == 10 && lunarDay == 1) {
            message = "寒衣节";
        } else if (lunarMonth == 12 && lunarDay == 8) {
            message = "腊八节";
        } else if (lunarMonth == 12 && lunarDay == 16) {
            message = "尾牙";
        } else if (lunarMonth == 12 && lunarDay == 23) {
            message = "北方小年";
        } else if (lunarMonth == 12 && lunarDay == 24) {
            message = "南方小年";
        } else if (lunarMonth == 12 && (LunarUtil.daysInMonth(lunarYear, lunarMonth) == 29 && lunarDay == 29 || LunarUtil.daysInMonth(lunarYear, lunarMonth) == 30 && lunarDay == 30)) {
            message = "除夕夜";
        } else if (!TextUtils.isEmpty(solarTerm) && "冬至".equals(solarTerm)) {
            message = solarTerm;
        } else if (solarMonth == 8) {
            if (solarDay == 1 && solarYear >= 1949) {
                message = "建军节";
            }
        }

        if (solarMonth == 4) {
            if (solarDay >= 4 && solarDay <= 6) {
                int compare;
                if (solarYear == 2000 && solarDay == 5) {

                } else if (solarYear <= 1999) {
                    compare = (int) ((double) (solarYear - 1900) * 0.2422D + 5.59D - (double) ((solarYear - 1900) / 4));
                    if (compare == solarDay) {
                        message = "清明";
                    }
                } else {
                    compare = (int) ((double) (solarYear - 2000) * 0.2422D + 4.81D - (double) ((solarYear - 2000) / 4));
                    if (compare == solarDay) {
                        message = "清明";
                    }
                }
            }
        }


        return message;
    }


    private static String getMotherOrFatherDay(int year, int month, int day) {
        if (month != 5 && month != 6) {
            return "";
        } else if ((month != 5 || day >= 8 && day <= 14) && (month != 6 || day >= 15 && day <= 21)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, 1);
            int weekDate = calendar.get(7);
            weekDate = weekDate == 1 ? 7 : weekDate - 1;
            switch (month) {
                case 5:
                    if (day == 15 - weekDate && year >= 1876) {
                        return "母亲节";
                    }
                    break;
                case 6:
                    if (day == 22 - weekDate && year >= 2005) {
                        return "父亲节";
                    }
                default:
                    break;
            }

            return "";
        } else {
            return "";
        }
    }

    private static String getThanksGivingDay(int year, int month, int day) {
        if (month != 11) {
            return "";
        } else if (month == 11 && day < 20) {
            return "";
        } else {
            LocalDate localDate = LocalDate.parse(year + "-" + month + "-" + day);
            int weekDate = CalendarUtil.countDate(localDate.toDate(), 4);
            return weekDate == 4 ? "感恩节" : "";
        }
    }

    public static Map<String, List<String>> holidayListOfYear = new LinkedHashMap<>();
    public static Map<String, List<String>> workdayListOfYear = new LinkedHashMap<>();
    //法定节假日 休息的日期
    public static List<String> holidayList = Arrays.asList(
            "2017-12-30", "2017-12-31", "2018-01-01", "2018-02-15", "2018-02-16", "2018-02-17", "2018-02-18", "2018-02-19", "2018-02-20", "2018-02-21", "2018-04-05",
            "2018-04-06", "2018-04-07", "2018-04-29", "2018-04-30", "2018-05-01", "2018-06-16", "2018-06-17", "2018-06-18", "2018-09-22", "2018-09-23", "2018-09-24",
            "2018-10-01", "2018-10-02", "2018-10-03", "2018-10-04", "2018-10-05", "2018-10-06", "2018-10-07", "2018-12-30", "2018-12-31", "2019-01-01", "2019-02-04",
            "2019-02-05", "2019-02-06", "2019-02-07", "2019-02-08", "2019-02-09", "2019-02-10", "2019-04-05", "2019-04-06", "2019-04-07", "2019-05-01", "2019-05-02",
            "2019-05-03", "2019-05-04", "2019-06-07", "2019-06-08", "2019-06-09", "2019-09-13", "2019-09-14", "2019-09-15", "2019-10-01", "2019-10-02", "2019-10-03",
            "2019-10-04", "2019-10-05", "2019-10-06", "2019-10-07");


    public static List<String> thisYearHolidayList = Arrays.asList("2020-01-01", "2020-01-24", "2020-01-25", "2020-01-26", "2020-01-27", "2020-01-28", "2020-01-29",
            "2020-01-30", "2020-01-31", "2020-02-01", "2020-02-02", "2020-04-04", "2020-04-05", "2020-04-06", "2020-05-01", "2020-05-02", "2020-05-03", "2020-05-04",
            "2020-05-05", "2020-06-25", "2020-06-26", "2020-06-27", "2020-10-01", "2020-10-02", "2020-10-03", "2020-10-04", "2020-10-05", "2020-10-06", "2020-10-07",
            "2020-10-08");


    //补班的日期
    public static List<String> workdayList = Arrays.asList(
            "2018-02-11", "2018-02-24", "2018-04-08", "2018-04-28", "2018-09-29", "2018-04-30", "2018-12-29", "2019-02-02", "2019-02-03", "2019-04-28", "2019-05-05",
            "2019-09-29", "2019-10-12");

    public static List<String> thisYearworkdayList = Arrays.asList("2020-01-19", "2020-04-26", "2020-05-09", "2020-06-28", "2020-09-27", "2020-10-10");

    static {
        holidayListOfYear.put("2020", thisYearHolidayList);
        workdayListOfYear.put("2020", thisYearworkdayList);
    }


    /**
     * 复活节
     */
    public static List<String> EasterDay = Arrays.asList("1900-4-15", "1901-4-7", "1902-3-30", "1903-4-12", "1904-4-3", "1905-4-23", "1906-4-15", "1907-3-31", "1908-4-19",
            "1909-4-11", "1910-3-27", "1911-4-16", "1912-4-7", "1913-3-23", "1914-4-12", "1915-4-4", "1916-4-23", "1917-4-8", "1918-3-31", "1919-4-20",
            "1920-4-4", "1921-3-27", "1922-4-16", "1923-4-1", "1924-4-20", "1925-4-12", "1926-4-4", "1927-4-17", "1928-4-8", "1929-3-31", "1930-4-20",
            "1931-4-5", "1932-3-27", "1933-4-16", "1934-4-1", "1935-4-21", "1936-4-12", "1937-3-28", "1938-4-17", "1939-4-9", "1940-3-24", "1941-4-13",
            "1942-4-5", "1943-4-25", "1944-4-9", "1945-4-1", "1946-4-21", "1947-4-6", "1948-3-28", "1949-4-17", "1950-4-9", "1951-3-25", "1952-4-13",
            "1953-4-5", "1954-4-18", "1955-4-10", "1956-4-1", "1957-4-21", "1958-4-6", "1959-3-29", "1960-4-17", "1961-4-2", "1962-4-22", "1963-4-14",
            "1964-3-29", "1965-4-18", "1966-4-10", "1967-3-26", "1968-4-14", "1969-4-6", "1970-3-29", "1971-4-11", "1972-4-2", "1973-4-22", "1974-4-14",
            "1975-3-30", "1976-4-18", "1977-4-10", "1978-3-26", "1979-4-15", "1980-4-6", "1981-4-19", "1982-4-11", "1983-4-3", "1984-4-22", "1985-4-7",
            "1986-3-30", "1987-4-19", "1988-4-3", "1989-3-26", "1990-4-15", "1991-3-31", "1992-4-19", "1993-4-11", "1994-4-3", "1995-4-16", "1996-4-7",
            "1997-3-30", "1998-4-12", "1999-4-4", "2000-4-23", "2001-4-15", "2002-3-31", "2003-4-20", "2004-4-11", "2005-3-27", "2006-4-16", "2007-4-8",
            "2008-3-23", "2009-4-12", "2010-4-4", "2011-4-24", "2012-4-8", "2013-3-31", "2014-4-20", "2015-4-5", "2016-3-27", "2017-4-16", "2018-4-1",
            "2019-4-21", "2020-4-12", "2021-4-4", "2022-4-17", "2023-4-9", "2024-3-31", "2025-4-20", "2026-4-5", "2027-3-28", "2028-4-16", "2029-4-1",
            "2030-4-21", "2031-4-13", "2032-3-28", "2033-4-17", "2034-4-9", "2035-3-25", "2036-4-13", "2037-4-5", "2038-4-25", "2039-4-10", "2040-4-1",
            "2041-4-21", "2042-4-6", "2043-3-29", "2044-4-17", "2045-4-9", "2046-3-25", "2047-4-14", "2048-4-5", "2049-4-18", "2050-4-10", "2051-4-2",
            "2052-4-21", "2053-4-6", "2054-3-29", "2055-4-18", "2056-4-2", "2057-4-22", "2058-4-14", "2059-3-30", "2060-4-18", "2061-4-10", "2062-3-26",
            "2063-4-15", "2064-4-6", "2065-3-29", "2066-4-11", "2067-4-3", "2068-4-22", "2069-4-14", "2070-3-30", "2071-4-19", "2072-4-10", "2073-3-26",
            "2074-4-15", "2075-4-7", "2076-4-19", "2077-4-11", "2078-4-3", "2079-4-23", "2080-4-7", "2081-3-30", "2082-4-19", "2083-4-4", "2084-3-26",
            "2085-4-15", "2086-3-31", "2087-4-20", "2088-4-11", "2089-4-3", "2090-4-16", "2091-4-8", "2092-3-30", "2093-4-12", "2094-4-4", "2095-4-24",
            "2096-4-15", "2097-3-31", "2098-4-20", "2099-4-12");

    public static void removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
    }


}
