package diagram.tool;

import diagram.Diagram;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import diagram.Diagram;
import diagram.DiagramUI;
import diagram.Figure;
import diagram.FigureEditor;
import diagram.Link;
import diagram.SelectionModel;
import uml.diagram.ClassEditorComponent;

/**
 * Created by Ihe-PC on 26/03/2015.
 */
public class RightClickTool extends  AbstractTool {

   private  Diagram currentDiagram;
   protected ClassEditorComponent editorComponent;
   protected  FigureEditor editor;
   protected Figure pressedFigure;
   protected MouseHandler mouseHandler = new MouseHandler();
   private Rectangle editorBounds = new Rectangle();


    @Override
    public void install(Diagram diagram) {

        diagram.addMouseListener(mouseHandler);
    }

    @Override
    public void uninstall(Diagram diagram) {

        diagram.removeMouseListener(mouseHandler);

    }

    protected  class MouseHandler extends MouseInputAdapter {
        private Link[] links = new Link[4];

        public void mousePressed(MouseEvent e) {

            if(SwingUtilities.isRightMouseButton(e) && !e.isConsumed() ) {

                currentDiagram = (Diagram)e.getSource();
                 pressedFigure = findPressedFigure(currentDiagram, e.getPoint());

                if(pressedFigure!=null) {
                    if (pressedFigure.getClass().getSimpleName().equals("ClassFigure") || pressedFigure.getClass().getSimpleName().equals("InterfaceFigure")) {

                        if(pressedFigure.getClass().getSimpleName().equals("ClassFigure")){

                            final JPopupMenu popupMenu = new JPopupMenu();
                            JMenu menu1 = new JMenu("Set title");
                            final JTextField fieldTitle = new JTextField(15);
                            JPanel panel1 = new JPanel();

                           JButton buttonSet = new JButton("set");
                           panel1.add(fieldTitle);
                           panel1.add(buttonSet);
                           menu1.add(panel1);

                            JMenu menu2 = new JMenu("Add field");
                            final JTextField fieldField = new JTextField(15);
                            JPanel panel2 = new JPanel();

                            JButton buttonField = new JButton("Add");
                            panel2.add(fieldField);
                            panel2.add(buttonField);
                            menu2.add(panel2);

                            JMenu menu3 = new JMenu("Add member");
                            final JTextField fieldMember = new JTextField(15);
                            JPanel panel3 = new JPanel();

                            JButton buttonMember = new JButton("Add");
                            panel3.add(fieldMember);
                            panel3.add(buttonMember);
                            menu3.add(panel3);


                            popupMenu.add(menu1);
                            popupMenu.addSeparator();
                            popupMenu.add(menu2);
                            popupMenu.addSeparator();
                            popupMenu.add(menu3);
                            popupMenu.addSeparator();
                            popupMenu.add(new JMenuItem("Format"));
                            popupMenu.addSeparator();
                            popupMenu.add(new JMenuItem("Copy"));
                            popupMenu.addSeparator();
                            popupMenu.add(new JMenuItem("Cut"));
                            popupMenu.addSeparator();
                            popupMenu.add(new JMenuItem("Delete"));
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());
                            buttonField.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    editor = currentDiagram.getFigureEditor(pressedFigure.getClass());

                                    editorComponent = (ClassEditorComponent) editor.getFigureEditorComponent(currentDiagram,pressedFigure,true);
                                    if(editorComponent == null)
                                        throw new RuntimeException("Bad FigureEditor!");

                                    fireToolStarted();

                                    // Configure the editing component
                                    editorBounds = (Rectangle)editor.getDecoratedBounds(currentDiagram, pressedFigure, editorBounds);

                                    editorComponent.setBounds(editorBounds.x, editorBounds.y,
                                            editorBounds.width, editorBounds.height);


                                    editorComponent.addField(fieldField.getText());

                                    currentDiagram.add(editorComponent);
                                    editorComponent.validate();
                                    editorComponent.setVisible(true);

                                    editorComponent.requestFocus();

                                    currentDiagram.getModel().setValue(pressedFigure, editor.getCellEditorValue());
                                    editor.stopCellEditing();

                                    editorBounds = (Rectangle)editorComponent.getBounds(editorBounds);
                                    currentDiagram.remove(editorComponent);

                                    // Update the area where the editor was
                                    currentDiagram.requestFocus();
                                    DiagramUI ui = (DiagramUI)currentDiagram.getUI();

                                    ui.damageFigure(pressedFigure);
                                    ui.repaintRegion(editorBounds);

                                    editorComponent.setFields("");

                                    currentDiagram = null;
                                    editorComponent = null;
                                    pressedFigure = null;
                                    fireToolFinished();
                                    popupMenu.setVisible(false);
                                }
                            });
                            buttonSet.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    editor = currentDiagram.getFigureEditor(pressedFigure.getClass());

                                    editorComponent = (ClassEditorComponent) editor.getFigureEditorComponent(currentDiagram,pressedFigure,true);
                                    if(editorComponent == null)
                                        throw new RuntimeException("Bad FigureEditor!");

                                    fireToolStarted();

                                    // Configure the editing component
                                    editorBounds = (Rectangle)editor.getDecoratedBounds(currentDiagram, pressedFigure, editorBounds);

                                    editorComponent.setBounds(editorBounds.x, editorBounds.y,
                                            editorBounds.width, editorBounds.height);


                                    editorComponent.setTitle(fieldTitle.getText());

                                    currentDiagram.add(editorComponent);
                                    editorComponent.validate();
                                    editorComponent.setVisible(true);

                                    editorComponent.requestFocus();

                                    currentDiagram.getModel().setValue(pressedFigure, editor.getCellEditorValue());
                                    editor.stopCellEditing();

                                    editorBounds = (Rectangle)editorComponent.getBounds(editorBounds);
                                    currentDiagram.remove(editorComponent);

                                    // Update the area where the editor was
                                    currentDiagram.requestFocus();
                                    DiagramUI ui = (DiagramUI)currentDiagram.getUI();

                                    ui.damageFigure(pressedFigure);
                                    ui.repaintRegion(editorBounds);
                                    editorComponent.setTitle("");

                                    currentDiagram = null;
                                    editorComponent = null;
                                    pressedFigure = null;
                                    fireToolFinished();
                                    popupMenu.setVisible(false);
                                }
                            });
                        }

                    }
                }

            }

        }

        protected Figure findPressedFigure(Diagram diagram, Point2D pt) {

            Figure pressedFigure = diagram.findFigure(pt);

            // If no figure was found by direct clicking, check link editor bounds
            // for label clicks
            if(pressedFigure == null) {

                // Request all links
                links = (Link[])diagram.getModel().toArray(links);
                for(int i=0; i < links.length && links[i] != null; i++) {

                    if(diagram.getModel().getValue(links[i]) == null)
                        continue;

                    // Check editor bounds
                    FigureEditor editor = diagram.getFigureEditor(links[i].getClass());
                    editorBounds = (Rectangle) editor.getDecoratedBounds(diagram, links[i], editorBounds);

                    if(editorBounds.contains(pt))
                        return links[i];

                }

            }

            return pressedFigure;

        }

    }
}
