package com.pw.utils;

import com.pw.biddingOntology.JobInitialPosition;

import java.util.Comparator;

public class JobInitialCompareDate implements Comparator<JobInitialPosition> {
    @Override
    public int compare(JobInitialPosition o1, JobInitialPosition o2) {
        return (int)(o1.getJobDate() - o2.getJobDate());
    }
}