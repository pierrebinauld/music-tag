package binauld.pierre.musictag.collection;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

public class MultipleBufferedList<E> implements List<E> {

    private Queue<List<E>> queue;

    private List<E> head;
    private List<E> tail;

    public MultipleBufferedList() {
        this.queue = new LinkedList<>();
        this.tail = new ArrayList<>();
        this.queue.add(tail);
        this.push();
        this.pull();
    }

    public List<E> getTail() {
        return tail;
    }

    public void push() {
        tail = new ArrayList<>(tail);
        queue.add(tail);
    }

    public void pull() {
        while(queue.size() > 1) {
            head = queue.poll();
        }
    }

    @Override
    public void add(int location, E object) {
        tail.add(location, object);
    }

    @Override
    public boolean add(E object) {
        return tail.add(object);
    }

    @Override
    public boolean addAll(int location, Collection<? extends E> collection) {
        return tail.addAll(location, collection);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return tail.addAll(collection);
    }

    @Override
    public void clear() {
        tail.clear();
    }

    @Override
    public boolean contains(Object object) {
        return head.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return head.containsAll(collection);
    }

    @Override
    public E get(int location) {
        return head.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return head.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return head.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return head.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return head.lastIndexOf(object);
    }

    @Override
    public ListIterator<E> listIterator() {
        return head.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int location) {
        return head.listIterator(location);
    }

    @Override
    public E remove(int location) {
        return tail.remove(location);
    }

    @Override
    public boolean remove(Object object) {
        return tail.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return tail.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return tail.retainAll(collection);
    }

    @Override
    public E set(int location, E object) {
        return tail.set(location, object);
    }

    @Override
    public int size() {
        return head.size();
    }

    @Override
    public List<E> subList(int start, int end) {
        return head.subList(start, end);
    }

    @Override
    public Object[] toArray() {
        return head.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return head.toArray(array);
    }
}