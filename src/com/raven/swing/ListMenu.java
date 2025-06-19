/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raven.swing;

import com.raven.event.EventMenuSelected;
import com.raven.model.Model_Menu;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

/**
 *
 * @author Lenovo
 */
public class ListMenu<E extends Object> extends JList<E> {

    private final DefaultListModel model;
    private int selectedIndex = -1;
    private int overIndex = -1;
    private EventMenuSelected event;

    public void addEventMenuSelected(EventMenuSelected event) {
        this.event = event;
    }

    public ListMenu() {
        model = new DefaultListModel();
        setModel(model);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
//                super.mousePressed(me);
                if (SwingUtilities.isLeftMouseButton(me)) {
                    int index = locationToIndex(me.getPoint());
                    Object o = model.getElementAt(index);
                    if (o instanceof Model_Menu) {
                        Model_Menu menu = (Model_Menu) o;
                        if (menu.getType() == Model_Menu.MenuType.MENU) {
                            selectedIndex = index;
                            if (event != null) {
                                event.selected(index);
                            }
                        }
                    } else {
                        selectedIndex = index;
                    }
                    repaint();
                }

            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                int index = locationToIndex(me.getPoint());
                if (index != overIndex) {
                    Object o = model.getElementAt(index);
                    if (o instanceof Model_Menu) {
                        Model_Menu menu = (Model_Menu) o;
                        if (menu.getType() == Model_Menu.MenuType.MENU) {
                            overIndex = index;
                        } else {
                            overIndex = -1;

                        }
                        repaint();
                    }
                }
            }
        });
    }

    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
        return new DefaultListCellRenderer() {

@Override
public Component getListCellRendererComponent(JList<?> jList, Object o, int index, boolean selected, boolean focus) {
    Model_Menu data;
    if (o instanceof Model_Menu) {
        data = (Model_Menu) o;
    } else {
        data = new Model_Menu(o + "", Model_Menu.MenuType.EMPTY);
    }

    JLabel label = new JLabel(data.getName());
    label.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));

    // Set font size and style
    Font font = label.getFont();
    font = font.deriveFont(selectedIndex == index ? Font.BOLD : Font.PLAIN, font.getSize() + 14); // Increase font size
    if (overIndex == index && data.getType() == Model_Menu.MenuType.MENU) {
        font = font.deriveFont(Font.BOLD | Font.ITALIC); // Set font to bold and italic if hovered and is a menu item
    }
    label.setFont(font);

    // Set text color to white
    label.setForeground(Color.WHITE);

    // Set background color to transparent
    label.setBackground(new Color(0, 0, 0, 0)); // Transparent color
    label.setOpaque(false); // Ensure background is transparent

    return label;
}





        };
    }

    public void addItem(Model_Menu data) {
        model.addElement(data);
    }
}
