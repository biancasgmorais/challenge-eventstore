package net.intelie.challenges;
import java.util.ArrayList;
import java.util.Iterator;
/**
 *
 * @author Bianca
 */
/*synchronized was used to keep thread-safe*/

public class EventFunctions implements EventStore, EventIterator{
    
    /*an Arraylist was used because it 
    is able to expand and shrink dynamically and can easily be accessed*/
    private ArrayList<Event> listEvents = new ArrayList();
    
    /*iterator was used to be a cursor in the list, 
    making it easier to use some functions, mainly to find values ??while scrolling*/
    private Iterator<Event> listEventsIterator = listEvents.iterator();
    
    //Constructor empty in case there is no need to change any object
    EventFunctions() {
      
    }
    
    /*using an Iterator directed to the EventIterator 
    Interface in order to return an Iterator*/
    EventFunctions(Iterator<Event> it){
        listEventsIterator = it;
    }

    public synchronized void insert(Event event){
        listEvents.add(event);
    }

    public synchronized void removeAll(String type){
      for (int i=0; i<this.listEvents.size(); i++){
        Event event=this.listEvents.get(i);
        if(event.type().equals(type)){
          listEvents.remove(i);
        }
      }
    }

    @Override
    public synchronized EventIterator query(String type, long startTime, long endTime) {
        ArrayList<Event> eventItArray = new ArrayList();;
        EventIterator ItEvent;
        //checks the timestamp between the start time and the end time
        while(listEventsIterator.hasNext()){
            if(listEventsIterator.next().type().equals(type) && 
                    (listEventsIterator.next().timestamp() >= startTime) &&
                    (listEventsIterator.next().timestamp() == endTime)){
                
                eventItArray.add(listEventsIterator.next());
                
               
            }    
        }
        
        Iterator<Event> iteratorArray = eventItArray.iterator();
        ItEvent = new EventFunctions(iteratorArray);
        return ItEvent;
    }

    @Override
    public synchronized boolean moveNext() {
        if(listEventsIterator.hasNext() == true){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public synchronized Event current() {
        if(moveNext() == true){
            Event current = listEventsIterator.next();
            return current;
        }
        return null;
    }

    @Override
    public synchronized void remove() {
        
        while(listEventsIterator.hasNext()){
            if(listEventsIterator.next() == current()){
                listEventsIterator.remove();
            }
        }
    }

    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
