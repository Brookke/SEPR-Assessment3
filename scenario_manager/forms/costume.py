#!/usr/bin/env python3

import os.path
import PyQt5.uic
from PyQt5.QtCore import Qt
from PyQt5 import QtCore, QtSql
from common import resource_icon, UI_SOURCE_LOCATION, DatabaseResourceForm

COSTUME_FORM_SOURCE = os.path.join(UI_SOURCE_LOCATION, "form_costume.ui")
UiCostumeForm = PyQt5.uic.loadUiType(COSTUME_FORM_SOURCE)[0]

class CostumeForm(UiCostumeForm, DatabaseResourceForm):
    def __init__(self, parent = None):
        super().__init__(parent)
        self.setupUi(self)
        self.relational_delegate = QtSql.QSqlRelationalDelegate(self.costume_table)

    def reset_model(self):
        if self.database:
            self.model = CostumeModel(self, self.database, self.resource_root)
            self.costume_table.setModel(self.model)
            self.costume_table.setItemDelegateForColumn(2, self.relational_delegate)
            self.costume_table.hideColumn(0)
        else:
            self.costume_table.setModel(None)

    @QtCore.pyqtSlot()
    def add_costume(self):
        if self.model:
            new_index = self.model.rowCount()
            self.model.insertRows(new_index, 1)
            self.costume_table.edit(self.model.index(new_index, 1))

    @QtCore.pyqtSlot()
    def remove_costumes(self):
        if self.model:
            for index in self.costume_table.selectedIndexes():
                self.model.removeRow(index.row(), index.parent())
            self.model.select()

class CostumeModel(QtSql.QSqlRelationalTableModel):
    def __init__(self, parent, database, resource_root = None):
        super().__init__(parent, database)
        self.setTable("costumes")
        self.setRelation(2, QtSql.QSqlRelation("resources", "id", "filename"))
        self.setJoinMode(QtSql.QSqlRelationalTableModel.LeftJoin)
        self.setEditStrategy(QtSql.QSqlTableModel.OnRowChange)
        self.select()
        self.resource_root = resource_root

    def data(self, index, role = Qt.DisplayRole):
        if index.column() == 2 and role == Qt.DecorationRole:
            return resource_icon(self, index, self.resource_root)
        else:
            return super().data(index, role)
