package com.nighthawk.csa.algorithm;

import com.nighthawk.csa.consoleUI.ConsoleMethods;
import com.nighthawk.csa.algorithm.genericDataModel.Alphabet;
import com.nighthawk.csa.algorithm.genericDataModel.Animal;
import com.nighthawk.csa.algorithm.genericDataModel.Cupcakes;
import com.nighthawk.csa.model.linkedlists.CircleQueue;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Circle Queue Driver takes a list of Objects and puts them into a Queue
 * @author     John Mortensen
 *
 */
@Getter
@Controller  // HTTP requests are handled as a controller, using the @Controller annotation
public class CircleQueueController {
    private CircleQueue queue;	// circle queue object
    private int count; // number of objects in circle queue
    private boolean animal = true;
    private Animal.KeyType animalKey = Animal.KeyType.combo;
    private boolean cake = true;
    private Cupcakes.KeyType cakeKey = Cupcakes.KeyType.combo;
    private boolean alpha = true;
    private Alphabet.KeyType alphaKey = Alphabet.KeyType.combo;

    /*
     * Circle queue constructor
     */
    public CircleQueueController()
    {
        count = 0;
        queue = new CircleQueue();
    }

    /*
     * Add any array of objects to the queue
     */
    public void addCQueue(Object[] objects)
    {
        System.out.println("Add " + objects.length);
        for (Object o : objects)
        {
            queue.add(o);
            System.out.println("Add: " + queue.getObject() + " " + queue);
            this.count++;
        }
        System.out.println();
    }

    /*
     * Delete/Clear all object in circle queue
     */
    public void deleteCQueue()
    {
        int length = this.count;
        System.out.println("Delete " + length);

        for (int i = 0; i<length; i++)
        {
            System.out.println("Delete: " + queue.delete() + " " + queue);
            this.count--;
        }
    }

    /*
     * String buffer for each row is created to support Thymeleaf (Interable could be alternative)
     */
    public List<String> getCQList()
    {
        List<String> log = new ArrayList<>();
        //travers each row, halting when first is re-encountered (circle queue halt)
        Object first = queue.getFirstObject();
        do {
            log.add(queue.getObject().toString());
        } while (queue.setNext() != first);
        return log;
    }

    /*
     GET request,, parameters are passed within the URI
     */
    @GetMapping("/data")
    public String data(Model model) {

        //initialize data from statics in code
        if (this.count == 0) {
            this.addCQueue(Animal.animalData());
            this.animal = true;
            this.animalKey = Animal.KeyType.combo;
            this.addCQueue(Cupcakes.cupCakeData());
            this.cake = true;
            this.cakeKey = Cupcakes.KeyType.combo;
            this.addCQueue(Alphabet.alphabetData());
            this.alpha = true;
            this.alphaKey = Alphabet.KeyType.combo;
        }
        model.addAttribute("cQc", this);

        return "algorithm/data"; //HTML render default condition
    }

    /*
     GET request,, parameters are passed within the URI
     */
    @PostMapping("/data")
    public String dataFilter(
            @RequestParam(value = "animal", required = false) String animal,
            @RequestParam(value = "cake", required = false) String cake,
            @RequestParam(value = "alpha", required = false) String alpha,
            Model model)
    {
        //re-init data according to check boxes selected
        count = 0;
        queue = new CircleQueue();
        if (animal != null) {
            this.addCQueue(Animal.animalData());
            this.animal = true;
            this.animalKey = Animal.KeyType.combo;
        } else {
            this.animal = false;
        }
        if (cake != null) {
            this.addCQueue(Cupcakes.cupCakeData());
            this.cake = true;
            this.cakeKey = Cupcakes.KeyType.combo;
        } else {
            this.cake = false;
        }
        if (alpha != null) {
            this.addCQueue(Alphabet.alphabetData());
            this.alpha = true;
            this.alphaKey = Alphabet.KeyType.combo;
        } else {
            this.alpha = false;
        }
        model.addAttribute("cQc", this);
        return "algorithm/data"; //HTML render default condition
    }

    /*
     * Show key objects/properties of circle queue
     */
    public void printCQueue()
    {
        //queue and object of queue all print via toString()
        ConsoleMethods.println("Size: " + count);
        ConsoleMethods.println("First Element: " + queue.getFirstObject());
        ConsoleMethods.println("Last Element: " + queue.getLastObject());
        ConsoleMethods.println("Full cqueue: " + queue);
        for (String line : this.getCQList()) {
            ConsoleMethods.println(line);
        }
        ConsoleMethods.println();
    }

    /*
     * Illustrate different Objects that can be placed on same Queue
     */
    public static void main(String[] args)
    {
        //queue
        CircleQueueController trial = new CircleQueueController();

        //add different types of objects to the same opaque queue
        trial.addCQueue(Animal.animalData());
        trial.addCQueue(Cupcakes.cupCakeData());
        trial.addCQueue(Alphabet.alphabetData());
        //display queue objects in queue order
        ConsoleMethods.println("Add order (all data)");
        trial.printCQueue();

        //sort queue objects by specific element within the object and display in sort order
        Animal.key = Animal.KeyType.name;
        Cupcakes.key = Cupcakes.KeyType.frosting;
        Alphabet.key = Alphabet.KeyType.letter;
        trial.queue.insertionSort();
        ConsoleMethods.println("Sorted order (key only)");
        trial.printCQueue();

        //display queue objects, changing output but not sort
        Animal.key = Animal.KeyType.combo;
        Cupcakes.key = Cupcakes.KeyType.combo;
        Alphabet.key = Alphabet.KeyType.combo;
        ConsoleMethods.println("Retain sorted order (all data)");
        trial.printCQueue();
        trial.queue.insertionSort();
        //display queue objects, changing sort order
        ConsoleMethods.println("Order by data type (all data)");
        trial.printCQueue();

        //delete queue objects
        ConsoleMethods.println("Delete from front (all data)");
        trial.deleteCQueue();
    }

}
