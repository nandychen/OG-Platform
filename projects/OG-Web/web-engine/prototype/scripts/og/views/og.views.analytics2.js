/*
 * @copyright 2009 - present by OpenGamma Inc
 * @license See distribution for license
 */
$.register_module({
    name: 'og.views.analytics2',
    dependencies: ['og.views.common.state', 'og.common.routes', 'og.common.gadgets.Gadgets_container'],
    obj: function () {
        var api = og.api.rest, routes = og.common.routes, module = this, view,
            page_name = module.name.split('.').pop(),
            check_state = og.views.common.state.check.partial('/' + page_name);
        module.rules = {load: {route: '/', method: module.name + '.load'}};
        return view = {
            check_state: og.views.common.state.check.partial('/'),
            default_details: function (args) {
                og.api.text({module: 'og.analytics.grid.configure_tash'}).pipe(function (markup) {
                    var template = Handlebars.compile(markup);
                    $('.OG-layout-analytics-center').html(template({}));
                });
            },
            load: function (args) {
                var Gc = og.common.gadgets.Gadgets_container,
                    count = 0, counter = function () {return ('100' + count++);},
                    gc_south = new Gc,
                    gc_r_north = new Gc,
                    gc_r_center = new Gc,
                    gc_r_south = new Gc,
                    timeseries_obj = function () {
                        var num = counter();
                        return {
                            gadget: og.common.gadgets.timeseries,
                            options: {id: 'DbHts~' + num, datapoints_link: false, child: true},
                            name: 'Timeseries ' + num,
                            margin: true
                        }
                    };
                gc_south.init('.OG-layout-analytics-south', [timeseries_obj(), timeseries_obj()]);
                gc_r_north.init('.OG-layout-analytics-dock-north', [timeseries_obj()]);
                gc_r_center.init('.OG-layout-analytics-dock-center');
                gc_r_south.init('.OG-layout-analytics-dock-south');
                gc_south.add([timeseries_obj()]);
                gc_r_center.add([timeseries_obj()]);
                gc_r_south.add([timeseries_obj()]);
                gc_r_north.add([timeseries_obj()]);
                // TODO THIS IS A GLOBAL, REMOVE IT LATER
                grid = new og.analytics.Grid({selector: '.OG-layout-analytics-center'});
            },
            load_item: function (args) {
                view.check_state({args: args, conditions: [{new_page: view.load}]});
                $('.OG-layout-analytics-center')
                    .html('<iframe width="100%" height="100%" src="gadget.ftl#/grid/foo/frame=true"></iframe>');
            },
            init: function () {for (var rule in view.rules) routes.add(view.rules[rule]);},
            rules: {
                load: {route: '/', method: module.name + '.load'},
                load_item: {route: '/:id', method: module.name + '.load_item'}
            }
        };
    }
});
