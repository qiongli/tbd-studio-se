package org.talend.oozie.scheduler.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.ViewPart;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.Element;
import org.talend.core.properties.tab.HorizontalTabFactory;
import org.talend.core.properties.tab.IDynamicProperty;
import org.talend.core.properties.tab.TalendPropertyTabDescriptor;
import org.talend.designer.core.ui.AbstractMultiPageTalendEditor;
import org.talend.designer.core.ui.ActiveProcessTracker;
import org.talend.designer.core.ui.views.properties.EElementType;
import org.talend.oozie.scheduler.i18n.Messages;
import org.talend.oozie.scheduler.ui.ExecuteJobComposite;
import org.talend.oozie.scheduler.ui.OozieMonitoringComposite;
import org.talend.oozie.scheduler.ui.ProcessContextComposite;

public class OozieSchedulerView extends ViewPart {

    private HorizontalTabFactory tabFactory;

    private TalendPropertyTabDescriptor currentSelectedTab;

    private boolean selectedPrimary = true;

    private ExecuteJobComposite executeJobComposite;

    private OozieMonitoringComposite monitoringComposite;

    private IDynamicProperty dc = null;

    private AbstractMultiPageTalendEditor part;

    private SashForm sash;

    private Composite parent;

    private Button moveButton;

    private ProcessContextComposite contextComposite;

    private OozieJobTrackerListener oozieJobTrackerListener = new OozieJobTrackerListener();

    public OozieSchedulerView() {
        ActiveProcessTracker.addJobTrackerListener(oozieJobTrackerListener);
        tabFactory = new HorizontalTabFactory();
    }

    @Override
    public void createPartControl(Composite parent) {
        this.parent = parent;
        parent.setLayout(new FillLayout());

        sash = new SashForm(parent, SWT.HORIZONTAL | SWT.SMOOTH);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        sash.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        GridLayout layout = new GridLayout();
        sash.setLayout(layout);

        createLeftContents(sash);
        createRightContents(sash);

        tabFactory.getTabComposite().layout();
        tabFactory.getTabbedPropertyComposite().getComposite().dispose();
        tabFactory.getTabbedPropertyComposite().pack();
        tabFactory.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                TalendPropertyTabDescriptor descriptor = (TalendPropertyTabDescriptor) selection.getFirstElement();

                if (descriptor == null) {
                    return;
                }

                if (currentSelectedTab != null && (currentSelectedTab.getCategory() != descriptor.getCategory())) {
                    for (Control curControl : tabFactory.getTabComposite().getChildren()) {
                        curControl.dispose();
                    }
                }

                if (currentSelectedTab == null || currentSelectedTab.getCategory() != descriptor.getCategory() || selectedPrimary) {

                    currentSelectedTab = descriptor;
                    createDynamicComposite(tabFactory.getTabComposite(), (Element) descriptor.getData(), descriptor.getCategory());
                    selectedPrimary = false;
                }
                refresh();
            }
        });
        setElement();
    }

    protected void createLeftContents(Composite parent) {
        Composite left = new Composite(parent, SWT.NONE);
        left.setLayout(new FillLayout());

        tabFactory.initComposite(left, false);
    }

    protected void createCenterContents(Composite parent) {
        FormData layouDatag = new FormData();
        layouDatag.left = new FormAttachment(0, 0);
        layouDatag.width = 32;
        layouDatag.top = new FormAttachment(0, 0);
        layouDatag.bottom = new FormAttachment(100, 0);

        final Composite buttonComposite = new Composite(parent, SWT.ERROR);
        buttonComposite.setLayoutData(layouDatag);
        buttonComposite.setLayout(new GridLayout());

        moveButton = new Button(buttonComposite, SWT.PUSH);
        moveButton.setText(">>"); //$NON-NLS-1$
        moveButton.setToolTipText("Hide contexts");
        final GridData layoutData = new GridData();
        layoutData.verticalAlignment = GridData.CENTER;
        layoutData.horizontalAlignment = GridData.CENTER;
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.grabExcessVerticalSpace = true;
        moveButton.setLayoutData(layoutData);
        regMoveButtonListener();
    }

    protected void createRightContents(Composite parent) {
        Composite right = new Composite(sash, SWT.NONE);
        right.setLayout(new FormLayout());

        createCenterContents(right);

        sash.setSashWidth(5);
        sash.setWeights(new int[] { 18, 5 });

        Composite cotextCom = new Composite(right, SWT.NONE);
        FormData layouDatag = new FormData();
        layouDatag.left = new FormAttachment(0, 32);
        layouDatag.right = new FormAttachment(100, 0);
        layouDatag.top = new FormAttachment(0, 0);
        layouDatag.bottom = new FormAttachment(100, 0);
        cotextCom.setLayoutData(layouDatag);
        cotextCom.setLayout(new GridLayout());
        contextComposite = new ProcessContextComposite(cotextCom, SWT.NONE);
        contextComposite.setBackground(right.getDisplay().getSystemColor(SWT.COLOR_WHITE));
    }

    private void regMoveButtonListener() {
        moveButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (moveButton.getText().equals(">>")) { //$NON-NLS-1$
                    sash.setWeights(new int[] { 23, 1 });
                    moveButton.setToolTipText(Messages.getString("ProcessComposite.showContext"));
                    moveButton.setText("<<");
                } else if (moveButton.getText().equals("<<")) { //$NON-NLS-1$
                    sash.setWeights(new int[] { 18, 5 });
                    moveButton.setToolTipText(Messages.getString("ProcessComposite.hideContext"));//$NON-NLS-1$
                    moveButton.setText(">>");
                }
            }
        });
    }

    public void createDynamicComposite(Composite parent, Element element, EComponentCategory category) {
        // jcomposite = this.processViewHelper.getProcessComposite(parent.getShell());
        if (category == EComponentCategory.SCHEDULE_4_HADOOP_EXECUTE_JOB) {
            executeJobComposite = new ExecuteJobComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_FOCUS);
            dc = executeJobComposite;
        } else if (category == EComponentCategory.SCHEDULE_4_HADOOP_MONITORING) {
            // debugTisProcessComposite = this.debugViewHelper.getDebugComposite(parent);
            // dc = debugTisProcessComposite;
            monitoringComposite = new OozieMonitoringComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
            dc = monitoringComposite;
        }
        refresh();
        if (dc != null) {
            dc.refresh();
        }
    }

    public void refresh() {
        getPart();
        if (part != null) {
            if (executeJobComposite != null && !executeJobComposite.isDisposed()) {
                executeJobComposite.setMultiPageTalendEditor(part);
            }
            tabFactory.setTitle("Job " + part.getProcess().getLabel(), null);
            this.setPartName("Oozie Scheduler (Job " + part.getProcess().getLabel() + ")");
            contextComposite.setProcess(part.getProcess());
            if (monitoringComposite != null && !monitoringComposite.isDisposed()) {
                monitoringComposite.setProcess(part.getProcess());
            }
        } else {
            if (executeJobComposite != null && !executeJobComposite.isDisposed()) {
                executeJobComposite.setMultiPageTalendEditor(null);
            }
            tabFactory.setTitle(Messages.getString("Title_name"), null);
            this.setPartName("Oozie Scheduler");
            contextComposite.setProcess(null);
            if (monitoringComposite != null && !monitoringComposite.isDisposed()) {
                monitoringComposite.setProcess(null);
            }
        }
        // setPartName("PartName");
        // tabFactory.setTitle("Hello Marvin", null);
    }

    private void getPart() {
        final IEditorPart activeEditor = getSite().getPage().getActiveEditor();
        if (activeEditor instanceof AbstractMultiPageTalendEditor) {
            part = (AbstractMultiPageTalendEditor) activeEditor;
        } else {
            part = null;
        }
    }

    private void setElement() {
        EComponentCategory[] categories = getCategories();
        final List<TalendPropertyTabDescriptor> descriptors = new ArrayList<TalendPropertyTabDescriptor>();
        for (EComponentCategory category : categories) {
            TalendPropertyTabDescriptor d = new TalendPropertyTabDescriptor(category);
            // d.setData(data);
            descriptors.add(d);
        }
        tabFactory.setInput(descriptors);
        tabFactory.setSelection(new IStructuredSelection() {

            public Object getFirstElement() {
                return null;
            }

            public Iterator iterator() {
                return null;
            }

            public int size() {
                return 0;
            }

            public Object[] toArray() {
                return null;
            }

            public List toList() {
                List<TalendPropertyTabDescriptor> d = new ArrayList<TalendPropertyTabDescriptor>();

                if (descriptors.size() > 0) {
                    if (currentSelectedTab != null) {
                        for (TalendPropertyTabDescriptor ds : descriptors) {
                            if (ds.getCategory() == currentSelectedTab.getCategory()) {
                                d.add(ds);
                                return d;
                            }
                        }
                    }
                    d.add(descriptors.get(0));
                }
                return d;
            }

            public boolean isEmpty() {
                return false;
            }

        });
    }

    private EComponentCategory[] getCategories() {
        EComponentCategory[] categories = EElementType.SCHEDULE_4_HADOOP.getCategories();
        final List<EComponentCategory> list = new ArrayList<EComponentCategory>(Arrays.asList(categories));
        return categories;
    }

    @Override
    public void setFocus() {
        this.parent.setFocus();
    }

    @Override
    public void dispose() {
        ActiveProcessTracker.removeJobTrackerListener(oozieJobTrackerListener);
        super.dispose();
    }
}