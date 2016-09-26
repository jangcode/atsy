function SettingsForm() {
    this.init = function (container, validationMessageKey) {
        if (typeof container === 'string') {
            container = $(container);
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
            } else if($this.find(".newPassword").val() === "" || $this.find(".newPasswordConfirm").val() === "" || $this.find(".oldPassword").val() === "") {
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
            container.find('.globalMessage .error-message').text(message);
            container.find('.globalMessage').show();
            container.addClass('has-error');
        }

        function hideError(container) {
            container.find('.globalMessage').hide();
            container.removeClass('has-error');
        }
    }
};

$(function () {
    new SettingsForm().init('#positions_section','settings.positions.error.empty');
    new SettingsForm().init('#channels_section','settings.channels.error.empty');
    new SettingsForm().init('#password_section','settings.password.error.empty');
});

function actionFormatter(value, row, index) {
    return [
        '<a class="edit ml10" href="javascript:void(0)" title="Edit">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>'
    ].join('');
}

window.positionsEvents = {
    'click .edit': function (e, value, row) {
        $('#position-form #position_name').val(row.name);
        $('#position-form #positionId').val(row.id);
    }
};
window.channelsEvents = {
    'click .edit': function (e, value, row) {
        $('#channel-form #channel_name').val(row.name);
        $('#channel-form #channelId').val(row.id);
    }
};
