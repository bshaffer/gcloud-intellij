/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.tools.intellij.elysium;

import com.intellij.ui.UI;
import com.intellij.ui.components.JBLabel;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

/**
 * Represents a single elysium project ui.
 */
class ProjectSelectorItem extends JBLabel {

  private Color textSelectionColor;
  private Color textNonSelectionColor;
  private Color hoverColor;

  public ProjectSelectorItem(@NotNull Color backgroundNonSelectionColor,
      @NotNull Color textSelectionColor, @NotNull Color textNonSelectionColor) {
    setBorder(BorderFactory.createEmptyBorder(2, 15, 2, 0));
    setOpaque(false);
    setHorizontalAlignment(SwingConstants.LEFT);
    setVerticalAlignment(SwingConstants.CENTER);
    this.textSelectionColor = textSelectionColor;
    this.textNonSelectionColor = textNonSelectionColor;

    hoverColor = UI.getColor("link.foreground");
    setBackground(backgroundNonSelectionColor);
  }

  public void initialize(String projectName, String projectId, boolean selected, boolean hovered) {
    setText(projectName + " (" + projectId + ")");
    if (selected) {
      setForeground(textSelectionColor);
    } else if (hovered) {
      setForeground(hoverColor);
    } else {
      setForeground(textNonSelectionColor);
    }
  }
}
