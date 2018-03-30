package SATII;

import BSO.DancesHeap;

import java.util.LinkedList;

/**
 * Created by ressay on 29/03/18.
 */
public class SATDances extends DancesHeap<SATSolution>
{


    SATInstance instance;
    int maxChances = 15;
    SATSolution bestDiversity = null;
    LinkedList<DiversityNode> diversityNodes = new LinkedList<>();

    public SATDances(SATInstance instance, int maxChances) {
        this.instance = instance;
        this.maxChances = maxChances;
    }



    @Override
    public void add(SATSolution solution)
    {
        super.add(solution);
//        addDiversity(solution);
    }

    int minVal = 100000;

    @Override
    public double evaluate(SATSolution solution) {
        int val = instance.getNumberOfClauses() - instance.getNumberOfClausesSatisfied(solution);
        if(val < minVal)
        {
            minVal = val;
            System.out.println("minimum so far: "+minVal);
        }
        return val;
    }

    @Override
    public SATSolution getBest()
    {
        SATSolution bestQuality = super.getBest();
        return bestQuality;
    }


    private static class DiversityNode
    {
        SATSolution solution;
        int diversityValue;

        public DiversityNode(SATSolution solution, int diversityValue) {
            this.solution = solution;
            this.diversityValue = diversityValue;
        }

        public int getDiversityValue() {
            return diversityValue;
        }

        public void setDiversityValue(int diversityValue) {
            this.diversityValue = diversityValue;
        }
    }


    public void addDiversity(SATSolution solution)
    {
        if (bestDiversity == null)
            bestDiversity = solution;
        else
        {
            int bestDiversityDistance = solution.length();
            int minDistance = solution.length();
            for(DiversityNode node : diversityNodes) {
                int distance = node.solution.distance(solution);
                // new diversity of node
                if (distance < node.getDiversityValue())
                    node.setDiversityValue(distance);
                // calculating diversity of inserted solution
                if(minDistance > distance)
                    minDistance = distance;
                // updating best diversity solution
                if(node.getDiversityValue() < bestDiversityDistance)
                {
                    bestDiversityDistance = node.getDiversityValue();
                    bestDiversity = node.solution;
                }
            }

            diversityNodes.add(new DiversityNode(solution,minDistance));
        }
    }
}
