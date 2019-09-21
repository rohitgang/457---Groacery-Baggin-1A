/

public class State{
    private List<Bag> totalBags;
    private List<Item> itemsNotAdded;

    public State(List<Bag> bags, List<Item> items){
        totalBags= bags;
        itemsNotAdded= items;
    }

    List<State> nextPossibleStates(){
        return  null;
    }

    boolean isComplete(){
        return false;
    }
}