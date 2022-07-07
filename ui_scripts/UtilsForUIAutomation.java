package ui_scripts;

import com.codeborne.selenide.Selenide;
import com.sun.jna.platform.win32.WinDef;
import mmarquee.automation.AutomationException;
import mmarquee.automation.*;
import mmarquee.automation.controls.*;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.title;
import static java.lang.String.format;

/**
 * Методы для работы с веб-приложениями, когда selenide не помогает :)
 */
public class UtilsForUIAutomation {

    /**
     * Вызов сочетания кнопок.
     *
     * @param buttons - коды кнопок. Для удобства лучше использовать класс KeyEvent, например: KeyEvent.VK_CONTROL, KeyEvent.VK_A и т.д.
     */
    public static void pressKeySequence(int... buttons) {
        Robot robot;
        try {
            robot = new Robot();
            robot.setAutoDelay(1000);
            for (int i = 0; i < buttons.length; i++) {
                robot.keyPress(buttons[i]);
            }
            for (int i = buttons.length - 1; i >= 0; i--) {
                robot.keyRelease(buttons[i]);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    /**
     * Поиск элементов в окне браузера по содержащемуся в них тексту
     *
     * @param elementText - Текст искомого элемента
     * @return - возвращает коллекцию объектов типа ArrayList<Element>, содержащую все найденные по тексту элементы
     */
    public static ArrayList<Element> findElementsWithText(String elementText) {
        Window window = null;
        ArrayList<Element> elemList = new ArrayList<>();
        try {
            window = UIAutomation.getInstance().getWindow(String.format("%s - Internet Explorer", title()), 1);
            List<AutomationBase> fullList = window.getChildren(true);

            for (AutomationBase el : fullList) {
                if (el.getElement().getName().equals(elementText)) {
                    elemList.add(el.getElement());
                }
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        }
        return elemList;
    }

    /**
     * Перемещение курсора мыши в координаты элемента на экране
     *
     * Примечание: При работе на java до 11 версии есть баг у класса awt.Robot, связанный
     * с масштабированием разрешения экрана в Win10. Для корректной работы оно должно быть 100%
     *
     * @param element - элемент, на который нужно переместить курсор мыши
     */
    public static void moveMouseOnElement(Element element) {
        Robot robot = null;
        try {
            robot = new Robot();
            WinDef.RECT position = element.getBoundingRectangle();
            robot.delay(2000);
            int elementWidth = position.right - position.left;
            int elementHeight = position.bottom - position.top;
            int x = position.left + elementWidth / 3;
            int y = position.top + elementHeight / 3;
            robot.delay(2000);
            robot.mouseMove(x, y);
        } catch (AWTException | AutomationException e) {
            e.printStackTrace();
        }
    }

}