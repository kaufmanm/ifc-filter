/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.filters.supervised.attribute.ifcfilter.numericalgorithme;

import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;

/**
 *
 * @author micha
 */
class PercentileRank extends NaturalRanking {

    public PercentileRank(NaNStrategy nanStrategy, TiesStrategy tiesStrategy) {
        super(nanStrategy, tiesStrategy);
    }

    @Override
    public double[] rank(double[] data) {
        double[] rank = super.rank(data);
        for (int i = 0; i < rank.length; i++) {
            rank[i] = rank[i] / (rank.length+1);
        }
        return rank;
    }
    
}
