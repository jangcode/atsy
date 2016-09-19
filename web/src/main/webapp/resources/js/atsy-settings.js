function SettingsForm() {
    this.init = function (container, validationMessageKey) {
        if (typeof container === 'string') {
            container = $(container);

            if (!container.length) {
                return;
            }
        }

        var form = container.find('form'),
            table = container.find('table'),
            addAction = container.find('.add');

        form.find('button[type="reset"]').add(addAction).on('click', function (event) {
            event.preventDefault();
            form.find('input').val('');
            hideError(container);
        });

        form.on('submit', function (event) {
            var $this = $(this);
            event.preventDefault();
            if ($this.find(".name").val() === "") {
                showError($this, window.messages[validationMessageKey]);
            } else {
                data = {};
                $this.serializeArray().map(function (x) {
                    data[x.name] = x.value;
                });
                hideError($this);
                $.ajax({
                    url: $this.attr('action'),
                    method: $this.attr('method'),
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    dataType: "json"
                }).done(function () {
                    if (table instanceof $) {
                        table.bootstrapTable('refresh');
                    }
                    $this.find('input').val('');
                }).error(function (xhr) {
                    var response = xhr.responseJSON;
                    showError($this, response.errorMessage);
                });
            }
        });

        function showError(container, message) {
            container.find('#errorMessageForCreating').text(message);
            container.find('#errorMessageForCreating').show();
            container.addClass('has-error');
        }

        function hideError(container) {
            container.find('#errorMessageForCreating').hide();
            container.removeClass('has-error');
        }
    }
};

$(function () {
    new SettingsForm().init('#positions_section','settings.positions.error.empty');
    new SettingsForm().init('#channels_section','settings.channels.error.empty');
});

function actionFormatter(value, row, index) {
    return [
        '<a class="edit ml10 little-space" href="javascript:void(0)" title="Edit">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>',

        '<a class="remove ml10 little-space" href="javascript:void(0)" title="Remove">',
        '<i class="glyphicon glyphicon-remove"></i>',
         '</a>',
    ].join('');
}

window.channelsEvents = {
    'click .edit': function (e, value, row) {
        $('#channel-form #channel_name').val(row.name);
        $('#channel-form #channelId').val(row.id);
    },
    'click .remove': function (e, value, row) {

         var self = $(this);
         var container = $('#channels_section');
         var options = {
            size: 'small',
            message: $.i18n.prop('question.delete.channel.js') + " (" + new Option(row.name).innerHTML + ")",
            animate: true,
            onEscape: function() {},
            buttons: {

                danger: {
                    label: $.i18n.prop('common.no.js'),
                    className: "btn-danger",
                    callback: function() {}
                },

                success: {
                      label: $.i18n.prop('common.yes.js'),
                      className: "btn-success",
                      callback: function() {
                          $.ajax({
                              type: 'DELETE',
                              url: "./delete?" + 'channelId=' + row.id,
                              cache: false,
                          }).done(function () {
                              var table = container.find('table');
                              self.closest('tr').remove();
                              hideError(container);
                          }).error(function (xhr) {
                              var response = xhr.responseJSON;
                              showError(container, $.i18n.prop('selected.channel.not.found.js'));
                          });
                      }
                 },
            },
         }
        bootbox.dialog(options);

        function showError(container, message) {
            container.find('#errorMessageForDeleting').text(message);
            container.find('#errorMessageForDeleting').show();
            container.addClass('has-error');
        }

        function hideError(container) {
            container.find('#errorMessageForDeleting').hide();
            container.removeClass('has-error');
        }
    }
};

window.positionsEvents = {
    'click .edit': function (e, value, row) {
        $('#position-form #position_name').val(row.name);
        $('#position-form #positionId').val(row.id);
    },

    'click .remove': function (e, value, row) {
             var self = $(this);
             var container = $('#positions_section');
             var options = {
                size: 'small',
                message: $.i18n.prop('question.delete.position.js') + " (" + new Option(row.name).innerHTML + ")",
                animate: true,
                onEscape: function() {},
                buttons: {

                    danger: {
                        label: $.i18n.prop('common.no.js'),
                        className: "btn-danger",
                        callback: function() {}
                    },

                    success: {
                          label: $.i18n.prop('common.yes.js'),
                          className: "btn-success",
                          callback: function() {
                              $.ajax({
                                  type: 'DELETE',
                                  url: "./delete?" + 'positionId=' + row.id,
                                  cache: false,
                              }).done(function() {
                                  var table = container.find('table');
                                  self.closest('tr').remove();
                                  hideError(container);
                              }).error(function (xhr) {
                                  var response = xhr.responseJSON;
                                  showError(container, $.i18n.prop('selected.position.not.found.js'));
                              });
                          }
                     },
                },
             }
            bootbox.dialog(options);

            function showError(container, message) {
                container.find('#errorMessageForDeleting').text(message);
                container.find('#errorMessageForDeleting').show();
                container.addClass('has-error');
            }

            function hideError(container) {
                container.find('#errorMessageForDeleting').hide();
                container.removeClass('has-error');
            }
        }
};