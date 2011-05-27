/**
 * 
 */
package org.irri.households.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author Chummy
 *
 */
public class VarFilter extends Composite {
	public VarFilter() {
		
		CellList<Object> cellList = new CellList<Object>(new AbstractCell<Object>(){
			@Override
			public void render(Context context, Object value, SafeHtmlBuilder sb) {
				// TODO
			}
		});
		initWidget(cellList);
	}

}
