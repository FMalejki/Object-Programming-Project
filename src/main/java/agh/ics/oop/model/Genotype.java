package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Genotype {
    private List<Integer> genes;
    private int activeGeneIndex;

    public Genotype(List<Integer> genes) {
        this.genes = genes;
        this.activeGeneIndex = new Random().nextInt(genes.size());
    }

    public Genotype(int numberOfGenes) {
        this.genes = generateGenes(numberOfGenes);
        this.activeGeneIndex = new Random().nextInt(genes.size());
    }

    public ArrayList<Integer> generateGenes(int numberOfGenes){
        List<Integer> randomList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfGenes; i++) {
            randomList.add(random.nextInt(8));
        }

        return new ArrayList<Integer>(randomList);
    }

    public int getActiveGene() {
        return genes.get(activeGeneIndex);
    }

    public void moveToNextGene() {
        if (new Random().nextInt(100) < 80) {
            activeGeneIndex = (activeGeneIndex + 1) % genes.size();
        } else {
            activeGeneIndex = new Random().nextInt(genes.size());
        }
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public void mutate() {

    }
}
