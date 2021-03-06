import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Run} from "./run";
import {JsonPageExtractor} from "../common/json-page-extractor";
import {Page} from "../common/page";

@Injectable()
export class RunService {

    constructor(private _http: Http) {}

    getRuns(testSuiteId: string, nextPage: string, fetchSize: number): Observable<Page<Run>> {

        let params: URLSearchParams = new URLSearchParams();
        if(nextPage != null) params.set('page', nextPage);
        if(fetchSize != null) params.set('fetchSize', fetchSize.toString());


        return this._http.get("/api/testsuites/" + testSuiteId + "/runs/overview", {
            search: params
        })
            .map(RunService.extractBodyPaged)
            .catch(RunService.extractError)
    }

    private static extractBodyPaged(response: Response): Page<Run> {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return JsonPageExtractor.extractFromJson(response.json(), RunService.convertJsonToRun);
    }

    private static extractError(error: any) {
        let errorMessage = error.message || "Server error";
        console.error(errorMessage);
        return Observable.throw(errorMessage);
    }

    private static convertJsonToRun(runJson: any): Run {
        let run = new Run();
        run.id = runJson.run.id;
        run.testSuite = runJson.run.testSuite;
        run.revision = runJson.run.revision;
        run.time = new Date(runJson.run.time);
        run.runResult = runJson.runStatistics.result;
        run.totalDurationMillis = runJson.runStatistics.totalDurationMillis;

        return run;
    }

}