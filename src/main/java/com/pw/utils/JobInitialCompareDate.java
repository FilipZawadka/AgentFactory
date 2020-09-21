package com.pw.utils;

import com.pw.biddingOntology.JobInitialPosition;

import java.util.Comparator;

public class JobInitialCompareDate implements Comparator<JobInitialPosition> {
    @Override
    public int compare(JobInitialPosition o1, JobInitialPosition o2) {
        String stringId1 = o1.getConversation();
        String stringId2 = o2.getConversation();

        Integer id1 = Integer.parseInt(stringId1.substring(3));
        Integer id2 = Integer.parseInt(stringId2.substring(3));

        if (id1 > id2) return 1;
        if (id1 < id2) return -1;
        return 0;
    }
}