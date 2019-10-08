package backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class stores the information of a download file
 *
 * @author Mohammad Mozafari
 */
public class DownloadFile
{
    private boolean queued, selected, infoShown;
    private double size;
    private int progressed = 0;
    private float downloadedKB;
    private String url;
    private String name;
    private String path;
    private Enums.DownloadingStatus status;
    private Date startDate = null;
    private Date endDate = null;

    /**
     * Sets the info of the download file.
     * @param url
     * @param name
     * @param size
     * @param path
     * @param status
     * @param queued
     */
    public DownloadFile(String url, String name, double size, String path, Enums.DownloadingStatus status, boolean queued)
    {
        this.size = size;
        this.url = url;
        this.name = name;
        this.path = path;
        this.status = status;
        this.queued = queued;
        this.infoShown = this.selected = false;
    }

    @Override
    public String toString()
    {
        return name + " : " + size + " : " + url;
    }

    public Enums.DownloadingStatus getStatus()
    {
        return status;
    }

    public String getName()
    {
        return name;
    }

    public double getSize()
    {
        return size;
    }

    public void setDownloadedKB(float downloadedKB)
    {
        this.downloadedKB = downloadedKB;
    }

    public boolean isQueued()
    {
        return queued;
    }

    public boolean isInfoShown()
    {
        return infoShown;
    }

    public void setInfoShown(boolean infoShown)
    {
        this.infoShown = infoShown;
    }

    public String getPath()
    {
        return path;
    }

    public String getUrl()
    {
        return url;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setStatus(Enums.DownloadingStatus status)
    {
        this.status = status;
    }

    public void setStartDate()
    {
        if (startDate == null)
            startDate = new Date();
    }

    public void setEndDate()
    {
        if (endDate == null)
            endDate = new Date();
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setProgressed(int progressed)
    {
        this.progressed = progressed;
    }

    public int getProgressed()
    {
        return progressed;
    }

    public void resetDate()
    {
        startDate = null;
        endDate = null;
    }

    public void setQueued(boolean queued)
    {
        this.queued = queued;
    }

    /**
     * This method sets an schedule for downloading the file.
     * @param time the string that contains the time
     * @param type the type of the schedule
     */
    public void scheduleDownload(String time, int type)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        Timer timer = new Timer();

        if (type == 1)
        {
            try
            {
                date = simpleDateFormat.parse(time);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            timer.schedule(new MyTimeTask(this), date);
        }
        else if (type == 2)
        {
            int milli = 60000 * Integer.parseInt(time);
            date = new Date((new Date()).getTime() + milli);
            timer.schedule(new MyTimeTask(this), date);
        }
    }

    /**
     * This class is used for doing a task at a certain time.
     */
    private class MyTimeTask extends TimerTask
    {
        DownloadFile file;

        public MyTimeTask(DownloadFile file)
        {
            this.file = file;
        }

        @Override
        public void run()
        {
            ActionCenter.startDownload(file);
            GuiManager.reloadContentWrapper();
            System.out.println("Downloaded");
        }
    }
}
