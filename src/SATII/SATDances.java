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
    int nbChances;
    int maxDiversity = -1;
    int maxDances = -1;
    SATSolution bestDiversity = null;
    LinkedList<DiversityNode> diversityNodes = new LinkedList<>();
    SATSolution previousBest = null;
    protected boolean choseDiversity = false;

    public SATDances(SATInstance instance, int maxChances) {
        this.instance = instance;
        this.maxChances = maxChances;
        nbChances = maxChances;
    }



    @Override
    public void add(SATSolution solution)
    {
        super.add(solution);
        addDiversity(solution);

        if(maxDances != -1 && heap.getCurrentSize() > maxDances)
            heap.removeLast();
        if(maxDiversity != -1 && diversityNodes.size() > maxDiversity)
            diversityNodes.removeFirst();
    }

    double minVal = 100000;

    @Override
    public double evaluate(SATSolution solution) {
        double val = solution.getEvaluation();
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
        choseDiversity = false;
        SATSolution bestQuality = super.getBest();
        SATSolution best = bestQuality;

        if(previousBest != null)
        {
            double df = bestQuality.getEvaluation() - previousBest.getEvaluation();
            if(df < 0)
                nbChances = maxChances;
            else
            {
                nbChances--;
                if(nbChances == 0)
                {
                    best = bestDiversity;
                    choseDiversity = true;
                    nbChances = maxChances;
                    heap.clear();
                }
            }
        }
        previousBest = best;
        return best;
    }

    public boolean didChoseDiversity()
    {
        return choseDiversity;
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
        if (bestDiversity == null) {
            bestDiversity = solution;
            diversityNodes.add(new DiversityNode(solution,solution.length()));
        }
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
                if(node.getDiversityValue() > bestDiversityDistance)
                {
                    bestDiversityDistance = node.getDiversityValue();
                    if(node.getDiversityValue() == minDistance)
                        bestDiversity = (node.solution.getEvaluation() > solution.getEvaluation())?
                                solution:node.solution;
                    else
                        bestDiversity = node.solution;
                }
                else if(node.getDiversityValue() == bestDiversityDistance)
                    bestDiversity = (node.solution.getEvaluation() > bestDiversity.getEvaluation())?
                            bestDiversity:node.solution;
            }

            diversityNodes.add(new DiversityNode(solution,minDistance));
        }
    }
}
