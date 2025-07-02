package org.example;

import java.util.EmptyStackException;
import java.util.Objects;

public class DobbelStackLenket<T> {
    private Node<T> hode1;
    private Node<T> hode2;
    private int size1;
    private int size2;
    /**
     * Representerer en node i den lenkede listen som brukes til å implementere stakkene.
     *
     * @param <T> Typen data som lagres i noden.
     */
    private static class Node<T>{
        private T data;
        private Node<T> neste;

        Node(T data) {
            this.data = Objects.requireNonNull(data, "Data kan ikke være null.");
            this.neste = null;
        }
    }

    /**
     * Pusher et element på toppen av stack 1.
     *
     * @param data Elementet som skal legges til. Kaster NullPointerException hvis data er null.
     */
    public void push1(T data) {
        Node<T> nyNode = new Node<>(data);
        nyNode.neste = hode1;
        hode1 = nyNode;
        size1++;
    }

    /**
     * Pusher et element på toppen av stack 2.
     *
     * @param data Elementet som skal legges til. Kaster NullPointerException hvis data er null.
     */
    public void push2(T data) {
        Node<T> nyNode = new Node<>(data);
        nyNode.neste = hode2;
        hode2 = nyNode;
        size2++;
    }

    /**
     * Popper elementet på toppen av stack 1.
     *
     * @return Elementet som ble poppet.
     * @throws EmptyStackException hvis stack 1 er tom.
     */
    public T pop1() {
        if (isEmpty1()) {
            throw new EmptyStackException();
        }
        T data = hode1.data;
        hode1 = hode1.neste;
        size1--;
        return data;
    }

    /**
     * Popper elementet på toppen av stack 2.
     *
     * @return Elementet som ble poppet.
     * @throws EmptyStackException hvis stack 2 er tom.
     */
    public T pop2() {
        if (isEmpty2()) {
            throw new EmptyStackException();
        }
        T data = hode2.data;
        hode2 = hode2.neste;
        size2--;
        return data;
    }
    /**
     * Sjekker om stack 1 er tom.
     *
     * @return True hvis stack 1 er tom, false ellers.
     */
    public boolean isEmpty1() {
        return size1 == 0;
    }

    public boolean isEmpty2() {
        return size2 == 0;
    }
    /**
     * Henter elementet på toppen av stack 1 uten å fjerne det.
     *
     * @return Elementet på toppen av stack 1.
     * @throws EmptyStackException hvis stack 1 er tom.
     */
    public T peek1() {
        if (isEmpty1()) {
            throw new EmptyStackException();
        }
        return hode1.data;
    }

    public T peek2() {
        if (isEmpty2()) {
            throw new EmptyStackException();
        }
        return hode2.data;
    }

    /**
     * Returnerer antall elementer i stack 1.
     *
     * @return Antall elementer i stack 1.
     */
    public int size1() {
        return size1;
    }
    public int size2() {
        return size2;
    }

    /**
     * Tømmer stack 1.
     */
    public void clear1() {
        hode1 = null;
        size1 = 0;
    }

    /**
     * Tømmer stack 2.
     */
    public void clear2() {
        hode2 = null;
        size2 = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stack 1: [");
        Node<T> current = hode1;
        while (current != null) {
            sb.append(current.data);
            if (current.neste != null) {
                sb.append(", ");
            }
            current = current.neste;
        }
        sb.append("]\n");

        sb.append("Stack 2: [");
        current = hode2;
        while (current != null) {
            sb.append(current.data);
            if (current.neste != null) {
                sb.append(", ");
            }
            current = current.neste;
        }
        sb.append("]\n");
        return sb.toString();
    }

    public static void main(String[] args) {
        DobbelStackLenket<Integer> dobbelStack = new DobbelStackLenket<>();

        dobbelStack.push1(10);
        dobbelStack.push1(20);
        dobbelStack.push2(30);
        dobbelStack.push2(40);

        System.out.println(dobbelStack.toString());

        System.out.println("Stack 1 Size: " + dobbelStack.size1());
        System.out.println("Stack 2 Size: " + dobbelStack.size2());

        System.out.println("Popped from Stack 1: " + dobbelStack.pop1());
        System.out.println("Peek on Stack 2: " + dobbelStack.peek2());
        System.out.println(dobbelStack);

        dobbelStack.clear1();
        System.out.println("Stack 1 is empty: " + dobbelStack.isEmpty1());
    }
}
