package com.example.stocktracker;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/*
 * @author: Hector Beltran & Marin Maksutaj
 * @description: This is the SharedViewModel class. It is used to share data between
 *             fragments.
 */
public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> stock = new MutableLiveData<String>();
    private final MutableLiveData<String> search = new MutableLiveData<String>();
    private final MutableLiveData<Integer> from = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> to = new MutableLiveData<Integer>();


    /*
    * The setStock method for the class. It is used to set the stock to be displayed.
    */
    public void setStock(String ticker) {
        stock.setValue(ticker);
    }
    /*
    * Get method for current stock.
    */
    public LiveData<String> getStock() {
        return stock;
    }

    /*
    * This method is used to save information on stock search that the user last made.
    * setter method for search term
    */
    public void setSearchTerm(String term) {
        search.setValue(term);
    }
    
    /*
    * Getter method for the search term.
    */
    public LiveData<String> getSearchTerm() {
        return search;
    }

    /*
    * Setter method for the from date.
    */
    public void setFrom(int term) { from.setValue(term);}

    /*
    * Getter method for the from index.
    */
    public LiveData<Integer> getFrom() {
        return from;
    }
    /*
    * Setter method for get index we are going to
    */
    public void setTo(int term) {
        to.setValue(term);
    }
    /*
    * Getter method for get index we are going to
    */
    public LiveData<Integer> getTo() {
        return to;
    }


}