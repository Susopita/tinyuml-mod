/**
 * Copyright 2007 Wei-ju Wu
 *
 * This file is part of TinyUML.
 *
 * TinyUML is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * TinyUML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TinyUML; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.tinyuml.ui.diagram;

import org.tinyuml.draw.AbstractCompositeNode;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.umldraw.shared.NoteElement;
import org.tinyuml.umldraw.structure.ClassElement;
import org.tinyuml.umldraw.structure.ComponentElement;
import org.tinyuml.umldraw.structure.PackageElement;

/**
 * Keeps a live count of supported diagram item types for the current editor.
 * The counter listens to editor add/remove events and recalculates the counts
 * from the diagram tree.
 *
 * @author Wei-ju Wu
 * @version 1.0
 */
public class DiagramItemCounter implements EditorStateListener {

  private DiagramEditor editor;
  private final Counts counts = new Counts();

  /**
   * Constructor.
   * @param anEditor the editor to observe
   */
  public DiagramItemCounter(DiagramEditor anEditor) {
    editor = anEditor;
    refresh();
  }

  /**
   * Returns the current label text.
   * @return the formatted label text
   */
  public String getLabelText() {
    return String.format("Total Items: %d, Package: %d, Class: %d, Component: %d, Note: %d",
      counts.total, counts.packages, counts.classes, counts.components,
      counts.notes);
  }

  /**
   * Refreshes the counts from the current editor.
   */
  public void refresh() {
    counts.reset();
    if (editor != null && editor.getDiagram() != null) {
      countItems(editor.getDiagram());
    }
  }

  /**
   * {@inheritDoc}
   */
  public void mouseMoved(EditorMouseEvent event) { }

  /**
   * {@inheritDoc}
   */
  public void stateChanged(DiagramEditor anEditor) { }

  /**
   * {@inheritDoc}
   */
  public void elementAdded(DiagramEditor anEditor) {
    editor = anEditor;
    refresh();
  }

  /**
   * {@inheritDoc}
   */
  public void elementRemoved(DiagramEditor anEditor) {
    editor = anEditor;
    refresh();
  }

  /**
   * Recursively counts supported element types.
   * @param element the diagram element
   */
  private void countItems(DiagramElement element) {
    if (element == null) {
      return;
    }
    if (element instanceof PackageElement) {
      counts.total++;
      counts.packages++;
    } else if (element instanceof ClassElement) {
      counts.total++;
      counts.classes++;
    } else if (element instanceof ComponentElement) {
      counts.total++;
      counts.components++;
    } else if (element instanceof NoteElement) {
      counts.total++;
      counts.notes++;
    }
    if (element instanceof AbstractCompositeNode) {
      for (DiagramElement child : ((AbstractCompositeNode) element).getChildren()) {
        countItems(child);
      }
    }
  }

  /**
   * Mutable counter state.
   */
  private static class Counts {
    int total;
    int packages;
    int classes;
    int components;
    int notes;

    void reset() {
      total = 0;
      packages = 0;
      classes = 0;
      components = 0;
      notes = 0;
    }
  }
}