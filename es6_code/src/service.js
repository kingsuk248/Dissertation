import { setTimeout } from "timers";
import Donut from './donut.js';
import BarChart from './barchart';
import Card from './card';
import { Subject } from 'rxjs';
//import { observe } from "rxjs-observe";

class Service {
    constructor(props) {
        this.donut = props.donutId;
        window.response = {};
        this.barchartSubject = new Subject();
        this.donutSubject = new Subject();
        this.overviewCardSubject = new Subject();
    }
    start(url) {
        var xhttp = new XMLHttpRequest(),
            that = this;

        xhttp.onreadystatechange = function() {
            if (this.status == 200) {
                if (this.readyState == 4) {
                    console.log(this.responseText);
                    that.parseResponse(JSON.parse(this.responseText));
                    setTimeout(() => {
                        document.body.setAttribute('state', 'home');
                    }, 1000);
                }
            } else if (this.status == 400 || this.status == 404 || this.status == 403) {
                console.log('Error ', this.status);
                document.body.setAttribute('state', 'error');
            } else {
                console.log('Somethng else - status -> ', this.status);
            }
        };
        xhttp.open("GET", url, true);
        xhttp.send();

    }
    parseResponse(json) {
        window.response = json;
        var channels = ["cost", "quote", "leads", "cpc", "avg_position", "clicks", "impressions"],
            result = {},
            keys = Object.keys(json);
        for (var k in keys) {
            var t = {},
                that = this,
                key = keys[k];
            if (key === 'search') {
                t.colour = 'rgba(94, 211, 110, 1)';
                t.colour2 = 'rgba(221, 237, 223, 1)';
                t.colour3 = 'rgba(191, 227, 195, 1)';
                t.prop1 = 'cost';
                t.prop2 = 'clicks';
            } else if (key === 'social') {
                t.colour = 'rgba(115, 117, 236, 1)';
                t.colour2 = 'rgba(230, 231, 253, 1)';
                t.colour3 = 'rgba(177, 178, 245, 1)';
                t.prop1 = 'clicks';
                t.prop2 = 'cpc';
            } else if (key === 'display') {
                t.colour = 'rgba(255, 118, 117, 1)';
                t.colour2 = 'rgba(232, 200, 195, 1)';
                t.colour3 = 'rgba(215, 164, 156, 1)';
                t.prop1 = 'cost';
                t.prop2 = 'impressions';
            }
            channels.forEach(item => {
                //console.log('forEach -> ', k, json[key], item);
                t[item] = that.extractChannelData(json[key], item);
            });
            result[key] = t;
        }
        //console.log(result);
        window.response = result;

        var counter = 0;
        var barchartData = [];
        for (var k in result) {
            counter++;
            var prop1 = result[k]['prop1'];
            var prop2 = result[k]['prop2'];
            var len = result[k][prop1].length;
            var labels = [];
            for (var i = 0; i < len; i++) {
                labels.push('T' + (i + 1));
            }
            window.response[k]['labels'] = labels;
            var dataarray = [];
            dataarray.push(result[k][prop1]);
            dataarray.push(result[k][prop2]);
            //Bar chart rendering logic --- need to move
            var id = ('barchart' + counter),
                //parent = document.getElementById('bar_chart_container'),
                data = {
                    'labels': labels,
                    'datasets': [
                        {
                            'data': result[k][prop2], 'label': prop2.toUpperCase(), 'type': 'line',
                            'backgroundColor': result[k]['colour2'], 'borderColor': result[k]['colour3'],
                            'pointBorderColor': result[k]['colour3'],
                            'pointBackgroundColor': result[k]['colour2'],
                            'yAxisID': 'R'
                        },
                        { 'data': result[k][prop1], 'label': prop1.toUpperCase(), 'backgroundColor': result[k]['colour'], 'barPercentage': 0.5, 'yAxisID': 'L' }]
                },
                options = {
                    scales: {
                        xAxes: [{
                            gridLines: {
                                offsetGridLines: true
                            }
                        }]
                    }
                },
                props = { 'id': id, 'title': k.toUpperCase(), 'data': data, 'options': options };
            barchartData.push(props);
            //(new BarChart(props)).render(parent);
        }
        this.extractDonutData('cost');
        this.barchartSubject.next(barchartData);
    }
    extractChannelData(arr, property) {
        var data = [];
        //console.log('extract->', arr, property);
        for (var a in arr) {
            //console.log(arr[a], arr[a][property]);
            data.push(arr[a][property]);
        }
        return data;
    }
    extractDonutData(property) {
        var data = [],
            labels = [],
            colours = [];
        for (var k in window.response) {
            var prop = window.response[k][property],
                aggr = 0;
            for (var i = 0; i < prop.length; i++) {
                aggr += prop[i];
            }
            window.response[k]['aggregate'] = aggr;
            labels.push(k);
            data.push(aggr);
            colours.push(window.response[k]['colour'])
        }
        window.donutData = { 'labels': labels, 'datasets': [{ 'data': data, 'backgroundColor': colours }] };
        this.donutSubject.next(donutData);
        //this.createDonut('donut', donutData);
        this.parseOverviewCardsData(['clicks', 'leads', 'impressions']);
    }
    parseOverviewCardsData(_props) {
        var result = {};
        _props.forEach((item, index) => {
            result[item] = 0;
            for (var i in window.response) {
                if (window.response[i][item]) {
                    result[item] += this.getAggr(window.response[i][item]);
                }
            }
        });
        console.log('Overview Cards -> ', result);
        this.overviewCardSubject.next(result);
    }
    getAggr(arr) {
        var sum = 0;
        for (var i in arr) {
            if (arr[i]) {
                sum += arr[i];
            }
        }
        return sum;
    }
    get donutSub() {
        return this.donutSubject;
    }
    get barchartSub() {
        return this.barchartSubject;
    }
    get overviewCardSub() {
        return this.overviewCardSubject;
    }
}
export default Service;