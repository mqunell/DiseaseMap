package edu.uw.tacoma.css.diseasemap.disease;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a general representation of a disease record, as we intend to use for to display in our visual.
 */
public final class DiseaseRecord {

    /*
     * This is a Map<Integer, Map<String, WeekInfo>>, where the outer map's key is the mapping of
     * WeekNumber -> Map<String, WeekInfo>, the Second map is mapping a String -> WeekInfo, where
     * the String is the name of the reporting location.
     */
    private Map<Integer, Map<String, WeekInfo>> weekInfo = new HashMap<>();

    //Name of the disease.
    private final String name;

    /**
     * This takes the list of WeekInfo and map it into a format that can be more readily used.
     *
     * @param name
     * @param weekInfo
     */
    public DiseaseRecord(String name, List<WeekInfo> weekInfo) {

        this.name = name;

        for (WeekInfo wi : weekInfo) {

            if (this.weekInfo.get(wi.getWeek()) == null) {
                this.weekInfo.put(wi.getWeek(), new HashMap<String, WeekInfo>());
            }

            this.weekInfo.get(wi.getWeek()).put(wi.getReportingArea(), wi);
        }
    }

    /**
     * Returns the name of this disease.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * This will return the Map associated with the specified week.
     * @param week
     * @return
     */
    public Map<String, WeekInfo> getInfoForWeek(Integer week) {
        return this.weekInfo.get(week);
    }

    /**
     *
     * We should put a method that returns the reporting locations.
     *
     */

    /**
     * Returns the number of week covered by this record.
     * @return
     */
    public Integer getWeeks() {
        return this.weekInfo.size();
    }

    /**
     * This is a manifestation of a single week's recording for a single location.
     */
    public static class WeekInfo {
        private final Integer year;
        private final Integer week;
        private final Integer infected;
        private final String reportingArea;

        public WeekInfo(Integer year, Integer week, Integer infected, String reportingArea) {
            this.year = year;
            this.week = week;
            this.infected = infected;
            this.reportingArea = reportingArea;
        }

        public String getReportingArea() {
            return reportingArea;
        }

        public Integer getWeek() {
            return this.week;
        }

        public Integer getInfected() {
            return infected;
        }
    }

}