/*
 * The MIT License
 *
 * Copyright (c) 2013, benas (md.benhassine@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.benas.todolist.web.pages.todo;

import io.github.todolist.core.domain.Priority;
import io.github.todolist.core.domain.Status;
import io.github.todolist.core.domain.Todo;
import io.github.todolist.core.domain.User;
import io.github.todolist.core.service.api.TodoService;
import io.github.benas.todolist.web.pages.user.Home;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Date;

/**
 * @author benas (md.benhassine@gmail.com)
 */
@SuppressWarnings("unused")
public class Update {

    @SessionState
    private User loggedUser;

    @Inject
    private TodoService todoService;

    @Inject
    private Messages messages;

    @Property
    private long id;

    @Property
    private String title;

    @Property
    private Priority priority;

    @Property
    private Status status;

    @Property
    private Date dueDate;

    @Persist
    @Property
    private Todo todo;

    @Property
    private String error;

    @InjectPage
    private io.github.benas.todolist.web.pages.Error errorPage;

    @Property
    @InjectComponent
    private Form updateTodoForm;

    @OnEvent(value = EventConstants.ACTIVATE)
    public Object init(long todoId) {
        todo = todoService.getTodoById(todoId);
        //TODO should use beanEditForm component, but must override all styles
        if (todo != null) {
            id = todo.getId();
            title = todo.getTitle();
            dueDate = todo.getDueDate();
            status = todo.getStatus();
            priority = todo.getPriority();
            return null;
        } else {
            errorPage.setError(messages.format("no.such.todo", todoId));
            return errorPage;
        }
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "updateTodoForm")
    public Object updateTodo() {
        todo.setTitle(title);
        todo.setDueDate(dueDate);
        todo.setPriority(priority);
        todo.setStatus(status);
        todoService.update(todo);
        return Home.class;
    }

}